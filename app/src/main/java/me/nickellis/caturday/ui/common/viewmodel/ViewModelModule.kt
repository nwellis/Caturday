package me.nickellis.caturday.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.nickellis.caturday.ui.images.CatImagesViewModel

@Module
abstract class ViewModelModule {

  @Binds @IntoMap @ViewModelKey(CatImagesViewModel::class)
  abstract fun bindCatImagesViewModel(catImagesViewModel: CatImagesViewModel): ViewModel

  @Binds
  abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}