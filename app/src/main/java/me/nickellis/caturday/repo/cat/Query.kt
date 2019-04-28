package me.nickellis.caturday.repo.cat


data class CatImagesQuery(
  val imageSize: CatImageSize = CatImageSize.Small,
  val page: Int = 0,
  val pageSize: Int
)

enum class CatImageSize {
  Max, Medium, Small, Thumbnail
}