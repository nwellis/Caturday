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
   * Gets images of cats. You can set this to ascending or descending but I really like the random functionality
   * that's the default.
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
   * Get the full cat image details.
   *
   * @param size `"full"`, `"med"`, `"small"`, `"thumb"`
   * @param page page location, starting at 0
   * @param limit min 1, max 100
   * @param format `"json"` or `"src"`
   *
   * @return gets a specific cat image
   *
   * @see [Doc](https://docs.thecatapi.com/api-reference/images/images-get)
   */
  @GET("$Prefix/images/search")
  fun getCatImage(
    @Query("image_id") imageId: String
  ): Call<ApiCatImageDetail>

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