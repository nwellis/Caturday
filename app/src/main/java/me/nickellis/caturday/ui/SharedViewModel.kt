package me.nickellis.caturday.ui

import androidx.lifecycle.MutableLiveData
import me.nickellis.caturday.AppExecutors
import me.nickellis.caturday.domain.CatBreed
import me.nickellis.caturday.ui.common.viewmodel.BaseViewModel
import javax.inject.Inject

class SharedViewModel @Inject constructor(
  appExecutors: AppExecutors
): BaseViewModel(appExecutors) {
  val selectedBreed = MutableLiveData<CatBreed>()
}

interface SharedViewModelProvider {
  val sharedViewModel: SharedViewModel
}