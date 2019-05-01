package me.nickellis.caturday.service.cat

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val Prefix = "/v1"

/**
 * Service for the cat [API](https://docs.thecatapi.com/api-reference). The cat API is the one stop shop for all
 * your cat needs. Check out its exposed functionality below.
 */
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
  fun getRandomCatImages(
    @Query("size") size: String,
    @Query("page") page: Int,
    @Query("limit") limit: Int,
    @Query("format") format: String = "json"
  ): Call<List<ApiCatImage>>

  /**
   * Gets all the cat breeds (paginated)
   *
   * @param attachBreed 1 for true, else 0 for false
   * @param page page location, starting at 0
   * @param limit min 1, max 100
   *
   * @return get all your cat breed facts right here
   *
   * @see [Doc](https://docs.thecatapi.com/api-reference/breeds/breeds-list)
   */
  @GET("$Prefix/breeds")
  fun getCatBreeds(
    @Query("attach_breed") attachBreed: Int = 0,
    @Query("page") page: Int,
    @Query("limit") limit: Int
  ): Call<List<ApiCatBreed>>
}