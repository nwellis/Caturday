package me.nickellis.pokedex

import android.app.Application


class PokedexApp : Application(), AppComponentProvider {

  override val component: AppComponent = DaggerAppComponent.builder()
    .appModule(AppModule(this))
    .build()
}