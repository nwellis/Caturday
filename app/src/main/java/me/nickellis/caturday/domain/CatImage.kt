package me.nickellis.caturday.domain

data class CatImage(
  val id: String,
  val url: String?,
  val details: CatImageDetails? = null
)

data class CatImageDetails(
  val filename: String?
)