package me.nickellis.caturday.service

import me.nickellis.caturday.BuildConfig
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