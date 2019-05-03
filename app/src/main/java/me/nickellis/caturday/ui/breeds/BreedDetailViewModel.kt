package me.nickellis.caturday.ui.breeds

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.nickellis.caturday.AppExecutors
import me.nickellis.caturday.R
import me.nickellis.caturday.domain.common.AppError
import me.nickellis.caturday.repository.cat.CatBreedsQuery
import me.nickellis.caturday.repository.cat.CatImageSize
import me.nickellis.caturday.repository.cat.CatImagesQuery
import me.nickellis.caturday.repository.cat.CatRepository
import me.nickellis.caturday.ui.common.state.NetworkState
import me.nickellis.caturday.ui.common.viewmodel.BaseViewModel
import javax.inject.Inject

/**
 * Not used anywhere, just an example of coroutines
 */
class BreedDetailViewModel @Inject constructor(
  appExecutors: AppExecutors,
  private val catRepository: CatRepository,
  context: Context
): BaseViewModel(appExecutors) {

  companion object {
    const val TAG = "BreedDetailViewModel"
  }

  private val _networkState = MutableLiveData<NetworkState>()
  val networkState = _networkState as LiveData<NetworkState>

  private val networkStateHandler = CoroutineExceptionHandler { _, throwable ->
    val error: AppError = when (throwable) {
      is AppError -> throwable
      else -> AppError(context.resources.getString(R.string.error_default), throwable)
    }
    _networkState.postValue(NetworkState.Error(error))
  }

  init {
  }

  /**
   * This function doesn't do anything useful, but I noticed I never actually used my coroutine scope. So
   * just to demonstrate how it would be used.
   */
  fun foo() {
    launch(networkStateHandler) {
      _networkState.postValue(NetworkState.Loading)

      val (catImages1, catImages2) = withContext(appExecutors.ioDispatcher) {
        val imagesRequest1 = async {
          catRepository.getRandomCatImages(CatImagesQuery(imageSize = CatImageSize.Medium)).await()
        }
        val imagesRequest2 = async {
          catRepository.getRandomCatImages(CatImagesQuery(imageSize = CatImageSize.Thumbnail)).await()
        }
        Pair(imagesRequest1.await(), imagesRequest2.await())
      }

      // Post to live data in most cases, I'm just logging it here though
      Log.i(TAG, "Got ${catImages1.size + catImages2.size} images!")

      _networkState.postValue(NetworkState.Success)
    }
  }


}