package me.nickellis.caturday.service.cat


open class ApiCatImage(
  val id: String? = null,
  val url: String? = null,
  //val categories: List<Categories>,
  val breeds: List<ApiCatBreed>
)

class ApiCatImageDetail(
  id: String?,
  url: String?,
  breeds: List<ApiCatBreed>,
  val filename: String?
): ApiCatImage(id, url, breeds)