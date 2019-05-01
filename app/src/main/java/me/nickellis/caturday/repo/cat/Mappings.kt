package me.nickellis.caturday.repo.cat

import me.nickellis.caturday.domain.CatBreed
import me.nickellis.caturday.domain.CatImage
import me.nickellis.caturday.ktx.ensureNonNullId
import me.nickellis.caturday.service.cat.ApiCatBreed
import me.nickellis.caturday.service.cat.ApiCatImage


fun ApiCatImage.toCatImage() = CatImage(
  id = id.ensureNonNullId("Found null ID from cat API get images"),
  url = url
)

fun ApiCatBreed.toCatBreed() = CatBreed(
  id = id.ensureNonNullId("Found null ID from cat API get images"),
  referenceImageId = referenceImageId,
  name = name,
  temperament = temperament,
  lifeSpan = lifeSpan,
  altNames = altNames,
  wikipediaUrl = wikipediaUrl,
  origin = origin,
  weightImperial = weightImperial,
  experimental = experimental > 0,
  hairless = hairless > 0,
  hypoallergenic = hypoallergenic > 0,
  rare = rare > 0,
  rex = rex > 0,
  shortLegs = shortLegs > 0,
  suppressedTail = suppressedTail > 0
)