package me.nickellis.pokedex.service

import me.nickellis.pokedex.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response


class CatApiHeadersInterceptor: Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request().newBuilder()
      .addHeader("x-api-key", BuildConfig.CatApiKey)
      .build()

    return chain.proceed(request)
  }
}