package me.nickellis.caturday.mocks

import androidx.lifecycle.Lifecycle
import me.nickellis.caturday.domain.common.AppError
import me.nickellis.caturday.repo.*

val NeedsResultOrErrorException = IllegalArgumentException("Supply the mock request with either a result or error, " +
    "both can't be null")

class MockRepositoryRequest<T>(val result: T? = null, val error: AppError? = null): RepositoryRequest<T> {
  private var _isExecuted = false
  private var _isCancelled = false

  override suspend fun await(): T {
    if (error != null)
      throw error

    return result ?: throw NeedsResultOrErrorException
  }

  override fun enqueue(callback: RepositoryCallback<T>) {
    val response = when {
      error != null -> ErrorResponse<T>(error)
      result != null -> SuccessResponse(result)
      else -> throw NeedsResultOrErrorException
    }

    callback(response)
  }

  override fun enqueueWith(lifecycle: Lifecycle, callback: RepositoryCallback<T>) {
    enqueue(callback)
  }

  override fun cancel(onCanceled: OnCancelledCallback?) {
    if (!_isCancelled) {
      onCanceled?.invoke(true)
      _isCancelled = true
    } else {
      onCanceled?.invoke(false)
    }
  }

  override val isExecuted: Boolean
    get() = _isExecuted
  override val isCancelled: Boolean
    get() = _isCancelled

}