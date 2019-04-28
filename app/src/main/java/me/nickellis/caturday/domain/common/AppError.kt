package me.nickellis.caturday.domain.common

/**
 * Global
 *
 * @param message A human readable message intended for users
 * @param cause Optional cause of the issue
 */
class AppError(
  override val message: String,
  override val cause: Throwable? = null
): Throwable(message, cause)