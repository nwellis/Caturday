package me.nickellis.caturday.ui.breeds

import android.os.Bundle
import androidx.lifecycle.LiveData
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
import me.nickellis.caturday.ui.common.viewmodel.PersistentViewModel
import javax.inject.Inject


class CatBreedsViewModel @Inject constructor(
  catRepository: CatRepository,
  appExecutors: AppExecutors
): BaseViewModel(appExecutors), PersistentViewModel {

  companion object {
    private const val ARG_QUERY = "catBreedsVM_query"
  }

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

  override fun saveTo(bundle: Bundle) {
    /**
     * We don't save the cat breeds themselves as the data could be quite large being an infinite scrolling list
     * and all.
     */
    bundle.putParcelable(ARG_QUERY, factory.query)
  }

  override fun restoreFrom(bundle: Bundle?) {
    bundle?.getParcelable<CatBreedsQuery>(ARG_QUERY)
      ?.let { query -> getCatBreeds(query) }
  }

  /**
   * Update the cat breed query. Since page and pageSize is ignored in favor of paged list config, this
   * actually doesn't do anything but I'll leave it for future functionality.
   */
  fun getCatBreeds(query: CatBreedsQuery): CatBreedsViewModel {
    if (query != factory.query) {
      factory.setQuery(query)
    }
    return this
  }

  fun retryFailedCall(): CatBreedsViewModel {
    factory.retryFailedCall()
    return this
  }

}