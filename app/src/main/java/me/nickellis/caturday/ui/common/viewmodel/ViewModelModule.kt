package me.nickellis.caturday.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.nickellis.caturday.ui.search.SearchCatsViewModel

@Module
abstract class ViewModelModule {

  @Binds @IntoMap @ViewModelKey(SearchCatsViewModel::class)
  abstract fun bindSearchCatsViewModel(searchCatsViewModel: SearchCatsViewModel): ViewModel

  @Binds
  abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}