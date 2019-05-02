package me.nickellis.caturday.repo

import android.content.Context
import dagger.Module
import dagger.Provides
import me.nickellis.caturday.repo.cat.ApiCatRepository
import me.nickellis.caturday.repo.cat.CatRepository
import me.nickellis.caturday.service.ErrorHandler
import me.nickellis.caturday.service.ServiceModule
import me.nickellis.caturday.service.cat.CatService
import retrofit2.Response
import javax.inject.Named
import javax.inject.Singleton


@Module(includes = [ServiceModule::class])
class RepositoryModule {

  @Singleton @Provides
  fun catRepository(
    @Named(ServiceModule.CatErrorConverter) errorHandler: ErrorHandler<Response<*>>,
    catService: CatService,
    context: Context
  ): CatRepository {
    return ApiCatRepository(errorHandler, catService, context)
  }

}