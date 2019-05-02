package me.nickellis.caturday.ui.images

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import me.nickellis.caturday.AppExecutors
import me.nickellis.caturday.domain.CatImage
import me.nickellis.caturday.repository.cat.CatImagesDataFactory
import me.nickellis.caturday.repository.cat.CatImagesQuery
import me.nickellis.caturday.repository.cat.CatRepository
import me.nickellis.caturday.ui.common.state.DataSourceState
import me.nickellis.caturday.ui.common.viewmodel.BaseViewModel
import javax.inject.Inject


class CatImagesViewModel @Inject constructor(
  catRepository: CatRepository,
  appExecutors: AppExecutors
): BaseViewModel(appExecutors) {

  private val factory: CatImagesDataFactory = CatImagesDataFactory(catRepository, appExecutors.networkIO)

  private val _query = MutableLiveData<CatImagesQuery>()
  val query = _query as LiveData<CatImagesQuery>
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

  fun setQuery(query: CatImagesQuery): CatImagesViewModel {
    if (query != _query.value) {
      _query.postValue(query)
      factory.setQuery(query)
    }
    return this
  }

  fun retryFailedCall(): CatImagesViewModel {
    factory.retryFailedCall()
    return this
  }

}