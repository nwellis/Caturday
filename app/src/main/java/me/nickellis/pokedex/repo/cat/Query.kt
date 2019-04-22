package me.nickellis.pokedex.repo.cat


data class CatImagesQuery(
  val imageSize: CatImageSize = CatImageSize.Medium,
  val page: Int,
  val pageSize: Int
)

enum class CatImageSize {
  Max, Medium, Small, Thumbnail
}