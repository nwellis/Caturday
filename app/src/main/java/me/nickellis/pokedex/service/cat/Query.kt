package me.nickellis.pokedex.service.cat


data class CatImagesQuery(
  val imageSize: CatImageSize = CatImageSize.Medium,
  val page: Int,
  val pageSize: Int
)

enum class CatImageSize {
  Max, Medium, Small, Thumbnail
}