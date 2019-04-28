package me.nickellis.caturday.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import me.nickellis.caturday.injector
import javax.inject.Inject


abstract class BaseFragment : Fragment() {

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injector.inject(this)
  }
}