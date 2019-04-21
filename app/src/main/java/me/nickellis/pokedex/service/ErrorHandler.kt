package me.nickellis.pokedex.service

import android.content.res.Resources
import me.nickellis.pokedex.R
import me.nickellis.pokedex.repo.RepositoryError
import me.nickellis.pokedex.service.cat.CatApiError
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response


interface ErrorHandler<T> {
  val defaultError: RepositoryError

  fun handle(error: T): RepositoryError
  fun handleFailure(t: Throwable): RepositoryError
}

class CatApiErrorHandler(
  private val resources: Resources,
  private val errorConverter: Converter<ResponseBody, CatApiError>
): ErrorHandler<Response<*>> {

  override val defaultError: RepositoryError = RepositoryError(
    message = resources.getString(R.string.error_default),
    cause = null
  )

  override fun handle(error: Response<*>): RepositoryError {
    val errorBody = error.errorBody()

    return errorBody
      ?.let { errorConverter.convert(it) }
      ?.let { RepositoryError(message = it.message, cause = HttpException(error)) }
      ?: RepositoryError(message = resources.getString(R.string.error_default), cause = HttpException(error))
  }

  override fun handleFailure(t: Throwable): RepositoryError {
    return RepositoryError(resources.getString(R.string.error_default), t)
  }
}