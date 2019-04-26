package me.nickellis.pokedex.ui.common.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

/**
 * All view models should inherit from BaseViewModel. This class implements a coroutine scope that lives alongside
 * the view model's lifecycle. Since this will most likely live beyond a [Fragment]'s  lifecycle it is highly
 * recommended that interactions between the model and the UI are done through [LiveData].
 *
 * @constructor Constructs the base implementation of a view model.
 */
abstract class BaseViewModel: ViewModel(), CoroutineScope {

  companion object {
    const val TAG = "BaseViewModel"
  }

  private val uncaughtExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Log.e(TAG, "Unhandled exception was caught, usage of the view model coroutine scope should provide its own" +
        "error handler", throwable)
  }

  private val job = SupervisorJob()
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job + uncaughtExceptionHandler

  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }
}