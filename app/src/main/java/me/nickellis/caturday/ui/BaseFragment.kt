package me.nickellis.caturday.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import me.nickellis.caturday.injector
import me.nickellis.caturday.ui.common.events.FragmentObserver
import me.nickellis.caturday.ui.common.viewmodel.PersistentViewModel
import me.nickellis.caturday.ui.common.viewmodel.PersistentViewModelManager
import javax.inject.Inject


abstract class BaseFragment : Fragment(), PersistentViewModelManager {

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  // This relies on the view model being tied to the fragment's restoration lifecycle. This might not
  // be apparent to the user, and should be revisited.
  private val persistentViewModels = mutableListOf<PersistentViewModel>()

  protected val fragmentObserver: FragmentObserver
    get() = activity as? FragmentObserver ?: throw Exception("$context is not a FragmentObserver")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injector.inject(this)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    persistentViewModels.forEach { viewModel ->
      viewModel.saveTo(outState)
    }
  }

  override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
    persistentViewModels.forEach { viewModel ->
      viewModel.restoreFrom(savedInstanceState)
    }
  }

  override fun managePersistenceOf(viewModel: PersistentViewModel) {
    persistentViewModels.add(viewModel)
  }
}