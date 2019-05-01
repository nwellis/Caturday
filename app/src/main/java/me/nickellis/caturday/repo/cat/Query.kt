package me.nickellis.caturday.repo.cat

interface PageRequest {
  val page: Int
  val pageSize: Int
}

data class CatImagesQuery(
  val imageSize: CatImageSize = CatImageSize.Small,
  override val page: Int = 0,
  override val pageSize: Int
): PageRequest

data class CatBreedsQuery(
  override val page: Int = 0,
  override val pageSize: Int
): PageRequest


enum class CatImageSize {
  Max, Medium, Small, Thumbnail
}