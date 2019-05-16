package me.nickellis.caturday.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CatBreed(
  val id: String,
  val referenceImageId: String?,
  val name: String?,
  val description: String?,
  val temperament: String?,
  val lifeSpan: String?,
  val altNames: String?,
  val wikipediaUrl: String?,
  val origin: String?,
  val weightImperial: String?,
  val catTraits: List<HasCatTrait>,
  val catTraitRatings: List<CatTraitRating>
): Parcelable