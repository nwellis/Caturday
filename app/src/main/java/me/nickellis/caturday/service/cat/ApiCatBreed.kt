package me.nickellis.caturday.service.cat

import com.google.gson.annotations.SerializedName


data class ApiCatBreed(
  @SerializedName("id") val id: String,
  @SerializedName("reference_image_id") val referenceImageId: String?,
  @SerializedName("name") val name: String?,
  @SerializedName("description") val description: String?,
  @SerializedName("temperament") val temperament: String?,
  @SerializedName("life_span") val lifeSpan: String?,
  @SerializedName("alt_names") val altNames: String?,
  @SerializedName("wikipedia_url") val wikipediaUrl: String?,
  @SerializedName("origin") val origin: String?,
  @SerializedName("weight_imperial") val weightImperial: String?,
  // 0 - 1 for booleans
  @SerializedName("experimental") val b_experimental: Int,
  @SerializedName("hairless") val b_hairless: Int,
  @SerializedName("hypoallergenic") val b_hypoallergenic: Int,
  @SerializedName("rare") val b_rare: Int,
  @SerializedName("rex") val b_rex: Int,
  @SerializedName("short_legs") val b_shortLegs: Int,
  @SerializedName("suppressed_tail") val b_suppressedTail: Int,
  @SerializedName("indoor") val b_indoor: Int,
  @SerializedName("lap") val b_lap: Int,
  // 0 - 5 for ranges
  @SerializedName("adaptability") val r_adaptability: Int,
  @SerializedName("affection_level") val r_affectionLevel: Int,
  @SerializedName("child_friendly") val r_childFriendly: Int,
  @SerializedName("dog_friendly") val r_dogFriendly: Int,
  @SerializedName("energy_level") val r_energyLevel: Int,
  @SerializedName("grooming") val r_grooming: Int,
  @SerializedName("health_issues") val r_healthIssues: Int,
  @SerializedName("intelligence") val r_intelligence: Int,
  @SerializedName("shedding_level") val r_sheddingLevel: Int,
  @SerializedName("social_needs") val r_socialNeeds: Int,
  @SerializedName("stranger_friendly") val r_strangerFriendly: Int,
  @SerializedName("vocalisation") val r_vocalisation: Int
)