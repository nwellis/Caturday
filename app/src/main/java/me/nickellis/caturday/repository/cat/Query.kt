package me.nickellis.caturday.repository.cat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

interface PageRequest {
  val page: Int
  val pageSize: Int
}

@Parcelize
data class CatImagesQuery(
  val imageSize: CatImageSize = CatImageSize.Small,
  override val page: Int = 0,
  override val pageSize: Int = 10
): PageRequest, Parcelable {
  fun next() = copy(page = page + 1)
}

@Parcelize
data class CatBreedsQuery(
  override val page: Int = 0,
  override val pageSize: Int = 10
): PageRequest, Parcelable {
  fun next() = copy(page = page + 1)
}

@Parcelize
enum class CatImageSize: Parcelable {
  Max, Medium, Small, Thumbnail
}