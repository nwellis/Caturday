package me.nickellis.caturday.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.nickellis.caturday.AppExecutors
import me.nickellis.caturday.domain.CatImage
import me.nickellis.caturday.repo.cat.CatImagesDataFactory
import me.nickellis.caturday.repo.cat.CatImagesQuery
import me.nickellis.caturday.repo.cat.CatRepository
import me.nickellis.caturday.ui.common.state.DataSourceState
import me.nickellis.caturday.ui.common.state.NetworkState
import me.nickellis.caturday.ui.common.viewmodel.BaseViewModel
import javax.inject.Inject


class SearchCatsViewModel @Inject constructor(
  catRepository: CatRepository,
  appExecutors: AppExecutors
): BaseViewModel(appExecutors) {

  private val factory: CatImagesDataFactory = CatImagesDataFactory(catRepository, appExecutors.networkIO)

  val catImages: LiveData<PagedList<CatImage>>
  val networkState: LiveData<DataSourceState>

  private val config = PagedList.Config.Builder()
    .setEnablePlaceholders(false)
    .setInitialLoadSizeHint(50)
    .setPageSize(50)
    .build()

  init {
    catImages = LivePagedListBuilder(factory, config)
      .setFetchExecutor(appExecutors.networkIO)
      .build()

    networkState = Transformations
      .switchMap(factory.mutableLiveData) { data -> data.networkState }
  }

  fun setQuery(query: CatImagesQuery): SearchCatsViewModel {
    launch { delay(100) }
    factory.setQuery(query)
    return this
  }

  fun retryFailedCall(): SearchCatsViewModel {
    factory.retryFailedCall()
    return this
  }

}