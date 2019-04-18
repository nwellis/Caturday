package me.nickellis.pokedex.service.cat

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val Prefix = "/v1"
interface CatService {
  @GET("$Prefix/images/search")
  fun searchCatImages(
    @Query("size") size: String,
    @Query("page") page: Int,
    @Query("limit") limit: Int,
    @Query("format") format: String = "json"
  ): Call<ApiCatImage>
}