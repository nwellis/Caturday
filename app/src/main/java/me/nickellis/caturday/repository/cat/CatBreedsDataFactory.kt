package me.nickellis.caturday.repository.cat

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import me.nickellis.caturday.domain.CatBreed
import me.nickellis.caturday.domain.common.AppError
import me.nickellis.caturday.repository.RepositoryRequest
import me.nickellis.caturday.ui.common.state.DataSourceState
import java.util.concurrent.Executor


class CatBreedsDataFactory(
  private val repository: CatRepository,
  private val executor: Executor
): DataSource.Factory<CatBreedsQuery, CatBreed>() {

  var query: CatBreedsQuery = CatBreedsQuery()
    private set

  val mutableLiveData: MutableLiveData<CatBreedsDataSource> = MutableLiveData()
  private val source: CatBreedsDataSource? get() = mutableLiveData.value

  override fun create(): DataSource<CatBreedsQuery, CatBreed> {
    return CatBreedsDataSource(repository, query, executor).apply {
      mutableLiveData.postValue(this)
    }
  }

  fun setQuery(query: CatBreedsQuery) {
    this.query = query
    source?.invalidate()
  }

  fun retryFailedCall() = source?.retryFailedCall()
}

class CatBreedsDataSource(
  private val repository: CatRepository,
  private val query: CatBreedsQuery,
  private val retryExecutor: Executor
): PageKeyedDataSource<CatBreedsQuery, CatBreed>() {

  private var retry: (() -> Any)? = null

  private var inflightRequest: RepositoryRequest<List<CatBreed>>? = null

  val networkState = MutableLiveData<DataSourceState>()

  fun retryFailedCall() {
    retry
      ?.also { retry = null }
      ?.let { retryExecutor.execute { it.invoke() } }
  }

  override fun loadInitial(
    params: LoadInitialParams<CatBreedsQuery>,
    callback: LoadInitialCallback<CatBreedsQuery, CatBreed>
  ) {
    networkState.postValue(DataSourceState.LoadInitial)

    try {
      val key = query.copy(page = 0, pageSize = params.requestedLoadSize)

      val catBreeds = runBlocking {
        repository.getCatBreeds(key).also { inflightRequest = it }.await()
      }

      callback.onResult(catBreeds, null, key.next())
      networkState.postValue(DataSourceState.Success)

    } catch (ex: CancellationException) {
      // Normal coroutine exception for cancellation, don't do any action
    } catch (ex: AppError) {
      retry = {
        loadInitial(params, callback)
      }
      networkState.postValue(DataSourceState.Error(ex))
    }
  }

  override fun loadAfter(params: LoadParams<CatBreedsQuery>, callback: LoadCallback<CatBreedsQuery, CatBreed>) {
    networkState.postValue(DataSourceState.LoadAfter)

    try {
      /**
       * The initial load size will probably be different than the after ones. So in the second pagination request
       * I'll have to fix this. The default is 3X the normal page size.
       *
       * Assumptions:
       *  1. initial load size is greater than the page size (If it's not that defeats its purpose)
       *  2. initial load size is multiple of page size
       */
      val key = if (params.requestedLoadSize < params.key.pageSize) {
        params.key.copy(page = params.key.pageSize/params.requestedLoadSize, pageSize = params.requestedLoadSize)
      } else {
        params.key
      }

      val catBreeds = runBlocking {
        repository.getCatBreeds(key).also { inflightRequest = it }.await()
      }

      callback.onResult(catBreeds, key.next())
      networkState.postValue(DataSourceState.Success)

    } catch (ex: CancellationException) {
      // Normal coroutine exception for cancellation, don't do any action
    } catch (ex: AppError) {
      retry = {
        loadAfter(params, callback)
      }
      networkState.postValue(DataSourceState.Error(ex))
    }
  }

  override fun loadBefore(
    params: LoadParams<CatBreedsQuery>,
    callback: LoadCallback<CatBreedsQuery, CatBreed>
  ) {
  }

  override fun invalidate() {
    inflightRequest?.cancel()
    super.invalidate()
  }
}