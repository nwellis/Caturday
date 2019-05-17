package me.nickellis.caturday.ui.breeds

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.nickellis.caturday.AppExecutors
import me.nickellis.caturday.domain.CatBreed
import me.nickellis.caturday.ui.common.viewmodel.BaseViewModel
import me.nickellis.caturday.ui.common.viewmodel.PersistentViewModel
import javax.inject.Inject

class BreedDetailViewModel @Inject constructor(
  appExecutors: AppExecutors
): BaseViewModel(appExecutors), PersistentViewModel {

  companion object {
    private const val ARG_BREED = "breedDetailVM_breed"
  }

  override fun saveTo(bundle: Bundle) {
    bundle.putParcelable(ARG_BREED, breed.value)
  }

  override fun restoreFrom(bundle: Bundle?) {
    bundle?.getParcelable<CatBreed>(ARG_BREED)?.let { breed.value = it }
  }

  private val breed = MutableLiveData<CatBreed>()

  fun getBreed(): LiveData<CatBreed> = breed

  fun setBreed(breed: CatBreed) {
    this.breed.value = breed
  }

}