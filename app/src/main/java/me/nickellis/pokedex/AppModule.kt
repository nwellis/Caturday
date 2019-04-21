package me.nickellis.pokedex

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable


@Module
class AppModule(private val app: PokedexApp) {

  @Provides @Reusable
  fun appContext(): Context {
    return app.applicationContext
  }

}