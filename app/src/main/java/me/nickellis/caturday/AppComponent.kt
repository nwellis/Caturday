package me.nickellis.caturday

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import dagger.Component
import me.nickellis.caturday.repository.RepositoryModule
import me.nickellis.caturday.repository.cat.CatRepository
import me.nickellis.caturday.ui.BaseActivity
import me.nickellis.caturday.ui.BaseFragment
import me.nickellis.caturday.ui.MainActivity
import me.nickellis.caturday.ui.common.viewmodel.ViewModelModule
import me.nickellis.caturday.ui.images.CatImagesFragment
import javax.inject.Singleton

interface AppComponentProvider {
  val component: AppComponent
}
val Activity.injector get() = (application as AppComponentProvider).component
val Fragment.injector get() = CaturdayApp.get().component

@Singleton
@Component(modules = [
  AppModule::class, RepositoryModule::class, ViewModelModule::class
])
interface AppComponent {

  //region Android
  fun appContext(): Context
  //endregion

  //region Repositories
  fun catRepository(): CatRepository
  //endregion

  fun inject(fragment: BaseFragment)

}