package me.nickellis.caturday.repository.cat

import android.content.res.Resources
import me.nickellis.caturday.R
import me.nickellis.caturday.domain.*
import me.nickellis.caturday.ktx.ensureNonNullId
import me.nickellis.caturday.service.cat.ApiCatBreed
import me.nickellis.caturday.service.cat.ApiCatImage
import me.nickellis.caturday.service.cat.ApiCatImageDetail


fun ApiCatImage.toCatImage(r: Resources) = CatImage(
  id = id.ensureNonNullId("Found null ID from cat API get images"),
  url = url,
  breeds = breeds.map { it.toCatBreed(r) }
)

fun ApiCatImageDetail.toCatImage(r: Resources) = CatImage(
  id = id.ensureNonNullId("Found null ID from cat API get image"),
  url = url,
  breeds = breeds.map { it.toCatBreed(r) },
  details = CatImageDetails(filename = filename)
)

fun ApiCatBreed.toCatBreed(r: Resources) = CatBreed(
  id = id.ensureNonNullId("Found null ID from cat API get breeds"),
  referenceImageId = referenceImageId,
  name = name,
  description = description,
  temperament = temperament,
  lifeSpan = lifeSpan,
  altNames = altNames,
  wikipediaUrl = wikipediaUrl,
  origin = origin,
  weightImperial = weightImperial,
  catTraits = listOf(
    HasCatTrait(r.getString(R.string.api_experimental), b_experimental > 0),
    HasCatTrait(r.getString(R.string.api_hairless), b_hairless > 0),
    HasCatTrait(r.getString(R.string.api_hypoallergenic), b_hypoallergenic > 0),
    HasCatTrait(r.getString(R.string.api_rare), b_rare > 0),
    HasCatTrait(r.getString(R.string.api_rex), b_rex > 0),
    HasCatTrait(r.getString(R.string.api_short_legs), b_shortLegs > 0),
    HasCatTrait(r.getString(R.string.api_suppressed_tail), b_suppressedTail > 0)
  ),
  catTraitRatings = listOf(
    CatTraitRating(r.getString(R.string.api_adaptability), r_adaptability.toLevel()),
    CatTraitRating(r.getString(R.string.api_affection_level), r_affectionLevel.toLevel()),
    CatTraitRating(r.getString(R.string.api_child_friendly), r_childFriendly.toLevel()),
    CatTraitRating(r.getString(R.string.api_dog_friendly), r_dogFriendly.toLevel()),
    CatTraitRating(r.getString(R.string.api_energy_level), r_energyLevel.toLevel()),
    CatTraitRating(r.getString(R.string.api_grooming), r_grooming.toLevel()),
    CatTraitRating(r.getString(R.string.api_health_issues), r_healthIssues.toLevel()),
    CatTraitRating(r.getString(R.string.api_intelligence), r_intelligence.toLevel()),
    CatTraitRating(r.getString(R.string.api_shedding_level), r_sheddingLevel.toLevel()),
    CatTraitRating(r.getString(R.string.api_social_needs), r_socialNeeds.toLevel()),
    CatTraitRating(r.getString(R.string.api_stranger_friendly), r_strangerFriendly.toLevel()),
    CatTraitRating(r.getString(R.string.api_vocalisation), r_vocalisation.toLevel())
  )
)

private fun Int.toLevel(): CatTraitLevel = when (this) {
  5 -> CatTraitLevel.Five
  4 -> CatTraitLevel.Four
  3 -> CatTraitLevel.Three
  2 -> CatTraitLevel.Two
  else -> CatTraitLevel.One
}