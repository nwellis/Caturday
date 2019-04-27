package me.nickellis.caturday

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.Reusable


@Module
class AppModule(private val app: CaturdayApp) {

  @Provides @Reusable
  fun appContext(): Context {
    return app.applicationContext
  }

}