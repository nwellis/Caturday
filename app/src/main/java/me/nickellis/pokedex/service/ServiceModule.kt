package me.nickellis.pokedex.service

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import me.nickellis.pokedex.BuildConfig
import me.nickellis.pokedex.service.cat.CatService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ServiceModule {

  @Provides @Singleton
  fun gson(): Gson {
    return GsonBuilder()
      .apply {
        if (BuildConfig.DEBUG)
          setPrettyPrinting()
      }
      .create()
  }

  @Provides @Singleton @Named("CatHttpClient")
  fun okhttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(CatApiHeadersInterceptor())
      .build()
  }

  @Provides @Singleton @Named("CatRetrofit")
  fun retrofit(
    @Named("CatHttpClient") client: OkHttpClient,
    gson: Gson
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl("https://api.thecatapi.com")
      .client(client)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .build()
  }

  @Provides @Singleton
  fun catService(@Named("CatRetrofit") retrofit: Retrofit): CatService {
    return retrofit.create(CatService::class.java)
  }

}