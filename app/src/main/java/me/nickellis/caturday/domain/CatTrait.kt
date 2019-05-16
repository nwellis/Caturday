package me.nickellis.caturday.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HasCatTrait(
  val name: String,
  val isPresent: Boolean
): Parcelable

@Parcelize
data class CatTraitRating(
  val name: String,
  val level: CatTraitLevel
): Parcelable

@Parcelize
enum class CatTraitLevel: Parcelable {
  One, Two, Three, Four, Five
}