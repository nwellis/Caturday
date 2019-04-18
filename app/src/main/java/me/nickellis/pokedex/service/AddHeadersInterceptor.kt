package me.nickellis.pokedex.service

import okhttp3.Interceptor
import okhttp3.Response


class AddHeadersInterceptor(): Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    return chain.proceed(chain.request())
  }
}