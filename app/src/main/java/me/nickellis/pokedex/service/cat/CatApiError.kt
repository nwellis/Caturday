package me.nickellis.pokedex.service.cat

/**
 * Error body returned by the cat api on error. For example:
 * ```
 *  {
 *    "message": "Not Found",
 *    "status": 404,
 *    "level": "info"
 *  }
 * ```
 */
data class CatApiError(
  val message: String,
  val status: Int,
  val level: String
)