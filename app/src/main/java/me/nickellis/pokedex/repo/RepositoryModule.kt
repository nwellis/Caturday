package me.nickellis.pokedex.repo

import android.content.Context
import dagger.Module
import dagger.Provides
import me.nickellis.pokedex.repo.cat.ApiCatRepository
import me.nickellis.pokedex.repo.cat.CatRepository
import me.nickellis.pokedex.service.cat.CatService
import javax.inject.Singleton


@Module
class RepositoryModule {

  @Singleton @Provides
  fun catRepository(context: Context, catService: CatService): CatRepository {
    return ApiCatRepository(context.applicationContext, catService)
  }

}