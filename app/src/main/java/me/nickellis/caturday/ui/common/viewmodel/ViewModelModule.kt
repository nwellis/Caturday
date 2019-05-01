package me.nickellis.caturday.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.nickellis.caturday.ui.SharedViewModel
import me.nickellis.caturday.ui.breeds.BreedDetailViewModel
import me.nickellis.caturday.ui.breeds.CatBreedsViewModel
import me.nickellis.caturday.ui.images.CatImagesViewModel

@Module
abstract class ViewModelModule {

  @Binds @IntoMap @ViewModelKey(SharedViewModel::class)
  abstract fun bindSharedViewModel(sharedViewModel: SharedViewModel): ViewModel

  @Binds @IntoMap @ViewModelKey(CatImagesViewModel::class)
  abstract fun bindCatImagesViewModel(catImagesViewModel: CatImagesViewModel): ViewModel

  @Binds @IntoMap @ViewModelKey(CatBreedsViewModel::class)
  abstract fun bindCatBreedsViewModel(catBreedsViewModel: CatBreedsViewModel): ViewModel

  @Binds @IntoMap @ViewModelKey(BreedDetailViewModel::class)
  abstract fun bindBreedDetailViewModel(breedDetailViewModel: BreedDetailViewModel): ViewModel

  @Binds
  abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}