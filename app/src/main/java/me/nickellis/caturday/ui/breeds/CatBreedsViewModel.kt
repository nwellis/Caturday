package me.nickellis.caturday.ui.breeds

import androidx.lifecycle.LiveData
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
import me.nickellis.caturday.ui.common.viewmodel.BaseViewModel
import javax.inject.Inject


class CatBreedsViewModel @Inject constructor(
  catRepository: CatRepository,
  appExecutors: AppExecutors
): BaseViewModel(appExecutors) {

  private val factory: CatBreedsDataFactory = CatBreedsDataFactory(catRepository, appExecutors.networkIO)

  val catBreeds: LiveData<PagedList<CatBreed>>
  val networkState: LiveData<DataSourceState>

  private val config = PagedList.Config.Builder()
    .setEnablePlaceholders(false)
    .setPageSize(20)
    .build()

  init {
    catBreeds = LivePagedListBuilder(factory, config)
      .setFetchExecutor(appExecutors.networkIO)
      .build()

    networkState = Transformations
      .switchMap(factory.mutableLiveData) { data -> data.networkState }
  }

  fun setQuery(query: CatBreedsQuery): CatBreedsViewModel {
    factory.setQuery(query)
    return this
  }

  fun retryFailedCall(): CatBreedsViewModel {
    factory.retryFailedCall()
    return this
  }

}