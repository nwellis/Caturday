package me.nickellis.caturday.repo

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import me.nickellis.caturday.AppExecutors
import me.nickellis.caturday.R
import me.nickellis.caturday.data.common.AppError
import me.nickellis.caturday.service.ErrorHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


/**
 * The response from a [RepositoryRequest]. This can be a success ([SuccessResponse]) or failure ([ErrorResponse]).
 */
sealed class RepositoryResponse<T>
class SuccessResponse<T>(val data: T): RepositoryResponse<T>()
class ErrorResponse<T>(val error: AppError): RepositoryResponse<T>()

typealias RepositoryCallback<T> = (response: RepositoryResponse<T>) -> Unit
typealias OnCancelledCallback = (succeeded: Boolean) -> Unit

/**
 * A representation of a request made to a repository. A suspend function is made available for coroutine use ([await]),
 * and for normal callback use there is [enqueue] and [enqueueWith]. Typical usage of enqueue:
 *
 * ```
 * repository.getFoo().enqueueWith(activity.lifecycle) { response ->
 *   when (response) {
 *     is SuccessResponse -> Log.i(TAG, "Success! ${response.data}")
 *     is ErrorResponse -> Log.e(TAG, "Failure, ${response.error}")
 *   }
 * }
 * ```
 *
 * Any await or enqueue call should be only executed **once**. Currently there is no clone method to help with
 * executing the same request more than once.
 */
interface RepositoryRequest<T> {

  /**
   * Sends the repository request and awaits the result. On completion a [RepositoryResponse] is returned indicating
   * whether the request was completed successfully or not.
   */
  suspend fun await(): T

  /**
   * Awaits for the result with the applications global IO dispatcher. On completion a [RepositoryResponse] is returned
   * indicating whether the request was completed successfully or not.
   *
   * @see [AppExecutors.ioDispatcher]
   */
  suspend fun awaitWithIO(): T = withContext(AppExecutors.ioDispatcher) { await() }

  /**
   * Asynchronously sends the repository request. On completion a [RepositoryResponse] is returned indicating
   * whether the request was completed successfully or not.
   *
   * @param callback Callback to communicate the response to
   */
  fun enqueue(callback: RepositoryCallback<T>)

  /**
   * Asynchronously sends the repository request. On completion a [RepositoryResponse] is returned indicating
   * whether the request was completed successfully or not. In addition it will observe the [Lifecycle] provided
   * and automatically cancel the request on the [Lifecycle.Event.ON_STOP] event.
   *
   * @param lifecycle Lifecycle to monitor
   * @param callback Callback to communicate the response to
   */
  fun enqueueWith(lifecycle: Lifecycle, callback: RepositoryCallback<T>)

  /**
   * If the request can be cancelled this will cancel it. In the event the request is not executed or has already
   * been cancelled this will be ignored.
   *
   * @param onCanceled optional callback to listen to.
   */
  fun cancel(onCanceled: OnCancelledCallback? = null)

  /**
   * Whether the request has started or not. If [await], [enqueue], or [enqueueWith] has been called this will
   * return true.
   */
  val isExecuted: Boolean

  /**
   * Whether [cancel] has been called on the request.
   */
  val isCancelled: Boolean
}

/**
 * Converts the retrofit [Call] into a [RepositoryRequest].
 * @see RetrofitRequest
 */
fun <Body, Model> Call<Body>.asRepositoryRequest(
  errorHandler: ErrorHandler<Response<*>>,
  mapper: (Body) -> Model
): RepositoryRequest<Model> {
  return RetrofitRequest(
    call = this,
    mapper = mapper,
    errorHandler = errorHandler
  )
}

/**
 * Implementation of a [RepositoryRequest] with a Retrofit [Call]. The normal [enqueue] method basically wraps
 * Retrofit's existing callback. On the other hand [await] uses a [suspendCancellableCoroutine] under the hood.
 * Invoking [cancel] will cancel the underlying [call].
 *
 * @param Body The body returned by the Retrofit call.
 * @param Model The result of the [RepositoryRequest].
 * @property call The network call to be use for acquiring the [Body].
 * @property mapper Upon successful completion this will be invoked in order to map the model.
 * @property errorHandler Handler to work with network and non-success issues.
 * @constructor Creates an implementation of [RepositoryRequest] with a Retrofit [Call].
 */
class RetrofitRequest<Body, Model>(
  private val call: Call<Body>,
  private val mapper: (Body) -> Model,
  private val errorHandler: ErrorHandler<Response<*>>
) : RepositoryRequest<Model>, LifecycleObserver {

  companion object {
    private const val TAG = "RetrofitRequest"
  }

  override val isExecuted: Boolean
    get() = call.isExecuted
  override val isCancelled: Boolean
    get() = call.isCanceled

  override suspend fun await(): Model {
    return suspendCancellableCoroutine { continuation ->
      enqueue { response ->
        when (response) {
          is SuccessResponse -> continuation.resume(response.data)
          is ErrorResponse -> continuation.resumeWithException(response.error)
        }
      }

      continuation.invokeOnCancellation {
        try {
          call.cancel()
        } catch (ex: Exception) {
          Log.w(TAG, "Unable to cancel request to ${call.request().url()}")
        }
      }
    }
  }

  override fun enqueueWith(lifecycle: Lifecycle, callback: RepositoryCallback<Model>) {
    lifecycle.addObserver(this@RetrofitRequest)
    enqueue(callback)
  }

  override fun enqueue(callback: RepositoryCallback<Model>) {
    call.enqueue(object : Callback<Body> {
      override fun onResponse(call: Call<Body>, response: Response<Body>) {
        if (response.isSuccessful) {
          try {
            @Suppress("UNCHECKED_CAST")
            callback(
              SuccessResponse(
                data = mapper((response.body() as Body))
              )
            )
          } catch (ex: Exception) {
            Log.e(TAG, "Failed to parse successful response from API at ${call.request().url()}. " +
                "Is the model definition correct?", ex)

            callback(ErrorResponse(
              error = errorHandler.handleFailure(ex)
            ))
          }
        } else {
          callback(ErrorResponse(
            error = errorHandler.handle(response)
          ))
        }
      }

      override fun onFailure(call: Call<Body>, t: Throwable) {
        if (call.isCanceled) return
        callback(ErrorResponse(
          error = errorHandler.handleFailure(t)
        ))
      }
    })
  }

  override fun cancel(onCanceled: OnCancelledCallback?) {
    if (isExecuted && !isCancelled) {
      call.cancel()
      onCanceled?.invoke(true)
    } else {
      onCanceled?.invoke(false)
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  protected fun onStop() {
    call.cancel()
  }
}