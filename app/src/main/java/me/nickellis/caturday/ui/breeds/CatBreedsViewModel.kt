package me.nickellis.caturday.ui.breeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import me.nickellis.caturday.AppExecutors
import me.nickellis.caturday.domain.CatBreed
import me.nickellis.caturday.repository.cat.CatBreedsDataFactory
import me.nickellis.caturday.repository.cat.CatBreedsQuery
import me.nickellis.caturday.repository.cat.CatRepository
import me.nickellis.caturday.ui.common.state.DataSourceState
import me.nickellis.caturday.ui.common.viewmodel.BaseViewModel
import javax.inject.Inject


class CatBreedsViewModel @Inject constructor(
  catRepository: CatRepository,
  appExecutors: AppExecutors
): BaseViewModel(appExecutors) {

  private val factory: CatBreedsDataFactory = CatBreedsDataFactory(catRepository, appExecutors.networkIO)

  val _query = MutableLiveData<CatBreedsQuery>()
  val query = _query as LiveData<CatBreedsQuery>

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

  fun getCatBreeds(): CatBreedsViewModel {
    if (_query.value == null) {
      CatBreedsQuery().let { newQuery ->
        _query.postValue(newQuery)
        factory.setQuery(newQuery)
      }
    }
    return this
  }

  fun retryFailedCall(): CatBreedsViewModel {
    factory.retryFailedCall()
    return this
  }

}