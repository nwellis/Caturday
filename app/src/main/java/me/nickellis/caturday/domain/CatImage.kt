package me.nickellis.caturday.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CatImage(
  val id: String,
  val url: String?,
  val breeds: List<CatBreed>,
  val details: CatImageDetails? = null
): Parcelable

@Parcelize
data class CatImageDetails(
  val filename: String?
): Parcelable