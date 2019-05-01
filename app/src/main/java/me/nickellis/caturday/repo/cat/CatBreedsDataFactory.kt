package me.nickellis.caturday.repo.cat

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import me.nickellis.caturday.domain.CatBreed
import me.nickellis.caturday.domain.common.AppError
import me.nickellis.caturday.repo.RepositoryRequest
import me.nickellis.caturday.ui.common.state.DataSourceState
import java.util.concurrent.Executor


class CatBreedsDataFactory(
  private val repository: CatRepository,
  private val executor: Executor
): DataSource.Factory<CatBreedsQuery, CatBreed>() {

  private var query: CatBreedsQuery = CatBreedsQuery(page = 0, pageSize = 10)

  val mutableLiveData: MutableLiveData<CatBreedsDataSource> = MutableLiveData()
  private val source: CatBreedsDataSource? = mutableLiveData.value

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
  var initialLoadSize = 0

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
    initialLoadSize = params.requestedLoadSize

    try {
      val key = query.copy(page = 0, pageSize = params.requestedLoadSize)

      val catBreeds = runBlocking {
        repository.getCatBreeds(key).also { inflightRequest = it }.await()
      }

      callback.onResult(catBreeds, null, query.copy(page = query.page + 1))
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
    initialLoadSize = params.requestedLoadSize

    try {
      // TODO: I know that the initialLoadSize != requestedLoadSize, so that will mess up this pagination. Fix!
      val key = query.copy(page = 0, pageSize = params.requestedLoadSize)

      val catBreeds = runBlocking {
        repository.getCatBreeds(key).also { inflightRequest = it }.await()
      }

      callback.onResult(catBreeds, query.copy(page = query.page + 1))
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