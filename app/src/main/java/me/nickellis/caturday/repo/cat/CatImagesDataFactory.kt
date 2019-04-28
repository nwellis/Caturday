package me.nickellis.caturday.repo.cat

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import me.nickellis.caturday.AppExecutors
import me.nickellis.caturday.domain.CatImage
import me.nickellis.caturday.domain.common.AppError
import me.nickellis.caturday.repo.RepositoryRequest
import me.nickellis.caturday.ui.common.state.DataSourceState
import java.util.concurrent.Executor


class CatImagesDataFactory(
  private val repository: CatRepository
): DataSource.Factory<CatImagesQuery, CatImage>() {

  val executor: Executor = AppExecutors.networkIO

  private var query: CatImagesQuery = CatImagesQuery(page = 0, pageSize = 10)

  val mutableLiveData: MutableLiveData<CatImagesDataSource> = MutableLiveData()
  private val source: CatImagesDataSource? = mutableLiveData.value

  override fun create(): DataSource<CatImagesQuery, CatImage> {
    return CatImagesDataSource(repository, query, executor).apply {
      mutableLiveData.postValue(this)
    }
  }

  fun setQuery(query: CatImagesQuery) {
    this.query = query
    source?.invalidate()
  }

  fun retryFailedCall() = source?.retryFailedCall()
}

class CatImagesDataSource(
  private val repository: CatRepository,
  private val query: CatImagesQuery,
  private val retryExecutor: Executor
): PageKeyedDataSource<CatImagesQuery, CatImage>() {

  private var retry: (() -> Any)? = null

  private var inflightRequest: RepositoryRequest<List<CatImage>>? = null

  val networkState = MutableLiveData<DataSourceState>()
  var initialLoadSize = 0

  fun retryFailedCall() {
    retry
      ?.also { retry = null }
      ?.let { retryExecutor.execute { it.invoke() } }
  }

  override fun loadInitial(
    params: LoadInitialParams<CatImagesQuery>,
    callback: LoadInitialCallback<CatImagesQuery, CatImage>
  ) {
    networkState.postValue(DataSourceState.LoadInitial)
    initialLoadSize = params.requestedLoadSize

    try {
      val key = query.copy(page = 0, pageSize = params.requestedLoadSize)

      val catImages = runBlocking {
        repository.getCatImages(key).also { inflightRequest = it }.await()
      }

      callback.onResult(catImages, null, query.copy(page = query.page + 1))

    } catch (ex: CancellationException) {
      // Normal coroutine exception for cancellation, don't do any action
    } catch (ex: AppError) {
      retry = {
        loadInitial(params, callback)
      }
      networkState.postValue(DataSourceState.Error(ex))
    }
  }

  override fun loadAfter(params: LoadParams<CatImagesQuery>, callback: LoadCallback<CatImagesQuery, CatImage>) {
    networkState.postValue(DataSourceState.LoadAfter)
    initialLoadSize = params.requestedLoadSize

    try {
      // TODO: I know that the initialLoadSize != requestedLoadSize, so that will mess up this pagination. Fix!
      val key = query.copy(page = 0, pageSize = params.requestedLoadSize)

      val catImages = runBlocking {
        repository.getCatImages(key).also { inflightRequest = it }.await()
      }

      callback.onResult(catImages, query.copy(page = query.page + 1))

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
    params: LoadParams<CatImagesQuery>,
    callback: LoadCallback<CatImagesQuery, CatImage>
  ) {
  }

  override fun invalidate() {
    inflightRequest?.cancel()
    super.invalidate()
  }
}