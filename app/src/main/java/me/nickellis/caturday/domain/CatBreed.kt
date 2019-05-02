package me.nickellis.caturday.domain


data class CatBreed(
  val id: String,
  val referenceImageId: String?,
  val name: String?,
  val temperament: String?,
  val lifeSpan: String?,
  val altNames: String?,
  val wikipediaUrl: String?,
  val origin: String?,
  val weightImperial: String?,
  val catTraits: List<HasCatTrait>,
  val catTraitRatings: List<CatTraitRating>
)