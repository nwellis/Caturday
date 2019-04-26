package me.nickellis.pokedex

import android.app.Activity
import android.content.Context
import dagger.Component
import me.nickellis.pokedex.repo.RepositoryModule
import me.nickellis.pokedex.repo.cat.CatRepository
import me.nickellis.pokedex.service.ErrorHandler
import me.nickellis.pokedex.service.ServiceModule
import me.nickellis.pokedex.service.cat.CatService
import me.nickellis.pokedex.ui.BaseActivity
import me.nickellis.pokedex.ui.MainActivity
import retrofit2.Response
import javax.inject.Named
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

  //region Cat Services
  @Named(ServiceModule.CatErrorConverter)
  fun catErrorHandler(): ErrorHandler<Response<*>>

  fun catService(): CatService
  //endregion

  //region Repositories
  fun catRepository(): CatRepository
  //endregion

  fun inject(activity: BaseActivity)
  fun inject(activity: MainActivity)

}