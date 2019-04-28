package me.nickellis.caturday.service

import android.content.res.Resources
import me.nickellis.caturday.R
import me.nickellis.caturday.data.common.AppError
import me.nickellis.caturday.service.cat.CatApiError
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response

/**
 * Error handling contract for all services. Each service could possibly have a different error body
 * that needs to be handled.
 */
interface ErrorHandler<T> {
  /**
   * Default error that represents an unknown or unidentified error.
   */
  val defaultError: AppError

  /**
   * Given an error, convert it to the applications global error.
   * @param error error that should be interpreted
   * @return Universal app error
   */
  fun handle(error: T): AppError

  /**
   * Given a throwable, convert it to the applications global error.
   * @param t throwable that should be interpreted
   * @return Universal app error
   */
  fun handleFailure(t: Throwable): AppError
}

class CatApiErrorHandler(
  private val resources: Resources,
  private val errorConverter: Converter<ResponseBody, CatApiError>
): ErrorHandler<Response<*>> {

  override val defaultError: AppError = AppError(
    message = resources.getString(R.string.error_default),
    cause = null
  )

  override fun handle(error: Response<*>): AppError {
    val errorBody = error.errorBody()

    return errorBody
      ?.let { errorConverter.convert(it) }
      ?.let { AppError(message = it.message, cause = HttpException(error)) }
      ?: AppError(message = resources.getString(R.string.error_default), cause = HttpException(error))
  }

  override fun handleFailure(t: Throwable): AppError {
    return AppError(resources.getString(R.string.error_default), t)
  }
}