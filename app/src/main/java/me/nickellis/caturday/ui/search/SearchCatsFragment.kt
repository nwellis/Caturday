package me.nickellis.caturday.ui.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import me.nickellis.caturday.R
import me.nickellis.caturday.injector
import me.nickellis.caturday.ui.BaseFragment
import javax.inject.Inject

class SearchCatsFragment : BaseFragment() {

  companion object {
    fun newInstance() = SearchCatsFragment()
  }

  private lateinit var viewModel: SearchCatsViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injector.inject(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.search_cats_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchCatsViewModel::class.java)
  }

}
