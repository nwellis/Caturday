package me.nickellis.caturday.repo.cat

interface PageRequest {
  val page: Int
  val pageSize: Int
}

data class CatImagesQuery(
  val imageSize: CatImageSize = CatImageSize.Small,
  override val page: Int = 0,
  override val pageSize: Int = 10
): PageRequest {
  fun next() = copy(page = page + 1)
}

data class CatBreedsQuery(
  override val page: Int = 0,
  override val pageSize: Int = 10
): PageRequest {
  fun next() = copy(page = page + 1)
}

enum class CatImageSize {
  Max, Medium, Small, Thumbnail
}