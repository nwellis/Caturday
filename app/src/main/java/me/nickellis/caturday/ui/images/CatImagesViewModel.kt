package me.nickellis.caturday.ui.images

import android.os.Bundle
import androidx.lifecycle.LiveData
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
import me.nickellis.caturday.ui.common.viewmodel.PersistentViewModel
import javax.inject.Inject


class CatImagesViewModel @Inject constructor(
  catRepository: CatRepository,
  appExecutors: AppExecutors
): BaseViewModel(appExecutors), PersistentViewModel {

  companion object {
    private const val ARG_QUERY = "catImagesVM_query"
  }

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

  override fun saveTo(bundle: Bundle) {
    /**
     * We don't save the cat images themselves as the data could be quite large being an infinite scrolling list
     * and all.
     */
    bundle.putParcelable(ARG_QUERY, factory.query)
  }

  override fun restoreFrom(bundle: Bundle?) {
    bundle?.getParcelable<CatImagesQuery>(ARG_QUERY)
      ?.let { query -> setQuery(query) }
  }

  /**
   * Update the cat images query. The paging arguments are ignored in favor of the paged list config, so the
   * only parameter that does anything is the [CatImagesQuery.imageSize] parameter.
   */
  fun setQuery(query: CatImagesQuery): CatImagesViewModel {
    if (query != factory.query) {
      factory.setQuery(query)
    }
    return this
  }

  fun retryFailedCall(): CatImagesViewModel {
    factory.retryFailedCall()
    return this
  }

}