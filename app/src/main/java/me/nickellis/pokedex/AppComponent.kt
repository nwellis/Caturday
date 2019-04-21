package me.nickellis.pokedex

import android.app.Activity
import android.content.Context
import dagger.Component
import me.nickellis.pokedex.repo.RepositoryModule
import me.nickellis.pokedex.repo.cat.CatRepository
import me.nickellis.pokedex.service.ServiceModule
import me.nickellis.pokedex.service.cat.CatService
import me.nickellis.pokedex.ui.MainActivity
import javax.inject.Singleton

interface AppComponentProvider {
  val component: AppComponent
}
val Activity.injector get() = (application as AppComponentProvider).component

@Singleton
@Component(modules = [
  AppModule::class, ServiceModule::class, RepositoryModule::class
])
interface AppComponent {

  //region Android
  fun appContext(): Context
  //endregion

  //region Services
  fun catService(): CatService
  //endregion

  //region Repositories
  fun catRepository(): CatRepository
  //endregion

  fun inject(activity: MainActivity)

}