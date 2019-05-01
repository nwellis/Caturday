package me.nickellis.caturday.service.cat

import com.google.gson.annotations.SerializedName


data class ApiCatBreed(
  @SerializedName("id") val id: String,
  @SerializedName("reference_image_id") val referenceImageId: String?,
  @SerializedName("name") val name: String?,
  @SerializedName("temperament") val temperament: String?,
  @SerializedName("life_span") val lifeSpan: String?,
  @SerializedName("alt_names") val altNames: String?,
  @SerializedName("wikipedia_url") val wikipediaUrl: String?,
  @SerializedName("origin") val origin: String?,
  @SerializedName("weight_imperial") val weightImperial: String?,
  // 0 - 1 for booleans
  @SerializedName("experimental") val experimental: Int,
  @SerializedName("hairless") val hairless: Int,
  @SerializedName("hypoallergenic") val hypoallergenic: Int,
  @SerializedName("rare") val rare: Int,
  @SerializedName("rex") val rex: Int,
  @SerializedName("short_legs") val shortLegs: Int,
  @SerializedName("suppressed_tail") val suppressedTail: Int
  // ------------------
)