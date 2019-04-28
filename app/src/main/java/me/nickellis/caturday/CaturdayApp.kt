package me.nickellis.caturday

import android.app.Application


class CaturdayApp : Application(), AppComponentProvider {

  override val component: AppComponent = DaggerAppComponent.builder()
    .appModule(AppModule(this))
    .build()

  override fun onCreate() {
    super.onCreate()
    INSTANCE = this
  }

  companion object {
    private var INSTANCE: CaturdayApp? = null
    fun get(): CaturdayApp = INSTANCE!!
  }
}