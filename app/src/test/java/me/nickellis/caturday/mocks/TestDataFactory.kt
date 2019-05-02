package me.nickellis.caturday.mocks

import me.nickellis.caturday.domain.CatBreed
import me.nickellis.caturday.domain.CatImage
import java.util.*


object TestDataFactory {
  
  private val random = Random()

  private val uniqueString get() = UUID.randomUUID().toString()
  private val uniqueBoolean get() = random.nextBoolean()

  fun newCatImage(id: String = uniqueString) = CatImage(
    id = id,
    url = uniqueString,
    breeds = emptyList()
  )

  fun newCatBreed(id: String = uniqueString) = CatBreed(
    id = uniqueString,
    referenceImageId = uniqueString,
    name = uniqueString,
    description = uniqueString,
    temperament = uniqueString,
    lifeSpan = uniqueString,
    altNames = uniqueString,
    wikipediaUrl = uniqueString,
    origin = uniqueString,
    weightImperial = uniqueString,
    catTraits = emptyList(),
    catTraitRatings = emptyList()
  )

}