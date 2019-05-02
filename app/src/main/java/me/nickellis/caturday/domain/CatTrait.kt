package me.nickellis.caturday.domain

data class HasCatTrait(
  val name: String,
  val isPresent: Boolean
)

data class CatTraitRating(
  val name: String,
  val level: CatTraitLevel
)

enum class CatTraitLevel {
  One, Two, Three, Four, Five
}