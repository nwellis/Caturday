package me.nickellis.caturday.ui.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import kotlinx.android.synthetic.main.search_cats_fragment.*

import me.nickellis.caturday.R
import me.nickellis.caturday.domain.CatImage
import me.nickellis.caturday.injector
import me.nickellis.caturday.ui.BaseFragment
import me.nickellis.caturday.ui.common.state.DataSourceState
import javax.inject.Inject

class SearchCatsFragment : BaseFragment() {

  companion object {
    const val TAG = "SearchCatsFragment"
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

    viewModel.catImages.observe(this, imagesObserver)
    viewModel.networkState.observe(this, networkObserver)
  }

  private val imagesObserver = Observer<PagedList<CatImage>> { images ->
    v_main.text = "Loaded!"
  }

  private val networkObserver = Observer<DataSourceState> { state ->
    when (state) {
      is DataSourceState.LoadInitial, is DataSourceState.LoadAfter -> Log.d(TAG, "Loading!")
      is DataSourceState.Success -> Log.d(TAG, "Success!")
      is DataSourceState.Error -> Log.d(TAG, "Error!")
    }
  }

}
