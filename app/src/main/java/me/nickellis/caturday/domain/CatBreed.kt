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
  val experimental: Boolean,
  val hairless: Boolean,
  val hypoallergenic: Boolean,
  val rare: Boolean,
  val rex: Boolean,
  val shortLegs: Boolean,
  val suppressedTail: Boolean
)