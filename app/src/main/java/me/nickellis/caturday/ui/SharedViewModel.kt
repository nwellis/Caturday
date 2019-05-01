package me.nickellis.caturday.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import me.nickellis.caturday.AppExecutors
import me.nickellis.caturday.domain.CatBreed
import me.nickellis.caturday.domain.CatImage
import me.nickellis.caturday.repo.cat.CatBreedsDataFactory
import me.nickellis.caturday.repo.cat.CatBreedsQuery
import me.nickellis.caturday.repo.cat.CatRepository
import me.nickellis.caturday.ui.common.state.DataSourceState
import me.nickellis.caturday.ui.common.state.NetworkState
import me.nickellis.caturday.ui.common.viewmodel.BaseViewModel
import javax.inject.Inject

class SharedViewModel @Inject constructor(
  appExecutors: AppExecutors
): BaseViewModel(appExecutors) {
  val selectedBreed = MutableLiveData<CatBreed>()
}

interface SharedViewModelProvider {
  val sharedViewModel: SharedViewModel
}