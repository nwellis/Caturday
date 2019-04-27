package me.nickellis.caturday.service.cat

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val Prefix = "/v1"
interface CatService {

  /**
   * Gets images of cats.
   *
   * @param size `"full"`, `"med"`, `"small"`, `"thumb"`
   * @param page page location, starting at 0
   * @param limit min 1, max 100
   * @param format `"json"` or `"src"`
   *
   * @return wonderful, wonderful pictures of cats!
   *
   * @see [Doc](https://docs.thecatapi.com/api-reference/images/images-search)
   */
  @GET("$Prefix/images/search")
  fun searchCatImages(
    @Query("size") size: String,
    @Query("page") page: Int,
    @Query("limit") limit: Int,
    @Query("format") format: String = "json"
  ): Call<List<ApiCatImage>>
}