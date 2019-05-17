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
import me.nickellis.caturday.domain.CatBreed
import me.nickellis.caturday.domain.common.AppError
import me.nickellis.caturday.repository.cat.CatImageSize
import me.nickellis.caturday.repository.cat.CatImagesQuery
import me.nickellis.caturday.repository.cat.CatRepository
import me.nickellis.caturday.ui.common.state.NetworkState
import me.nickellis.caturday.ui.common.viewmodel.BaseViewModel
import javax.inject.Inject

class BreedDetailViewModel @Inject constructor(
  appExecutors: AppExecutors
): BaseViewModel(appExecutors) {

  private val breed = MutableLiveData<CatBreed>()

  fun getBreed(): LiveData<CatBreed> = breed

  fun setBreed(breed: CatBreed) {
    this.breed.value = breed
  }

}