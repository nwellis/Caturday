package me.nickellis.pokedex.repo

import android.content.Context
import dagger.Module
import dagger.Provides
import me.nickellis.pokedex.repo.cat.ApiCatRepository
import me.nickellis.pokedex.repo.cat.CatRepository
import me.nickellis.pokedex.service.ErrorHandler
import me.nickellis.pokedex.service.ServiceModule
import me.nickellis.pokedex.service.cat.CatApiError
import me.nickellis.pokedex.service.cat.CatService
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import javax.inject.Named
import javax.inject.Singleton


@Module
class RepositoryModule {

  @Singleton @Provides
  fun catRepository(
    @Named(ServiceModule.CatErrorConverter) errorHandler: ErrorHandler<Response<*>>,
    catService: CatService
  ): CatRepository {
    return ApiCatRepository(errorHandler, catService)
  }

}