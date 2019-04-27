package me.nickellis.caturday

import android.app.Application


class CaturdayApp : Application(), AppComponentProvider {

  override val component: AppComponent = DaggerAppComponent.builder()
    .appModule(AppModule(this))
    .build()
}