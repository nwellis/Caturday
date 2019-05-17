package me.nickellis.caturday.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import me.nickellis.caturday.injector
import me.nickellis.caturday.ui.common.events.FragmentObserver
import java.lang.Exception
import javax.inject.Inject


abstract class BaseFragment : Fragment() {

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  protected val fragmentObserver: FragmentObserver
    get() = activity as? FragmentObserver ?: throw Exception("$context is not a FragmentObserver")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injector.inject(this)
  }
}