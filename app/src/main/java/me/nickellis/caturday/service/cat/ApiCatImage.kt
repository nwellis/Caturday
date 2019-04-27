package me.nickellis.caturday.service.cat


data class ApiCatImage(
  val id: String? = null,
  val url: String? = null,
  val width: Int = 0,
  val height: Int = 0
  //val categories: List<Categories>,
  //val breeds: List<Breeds>
)