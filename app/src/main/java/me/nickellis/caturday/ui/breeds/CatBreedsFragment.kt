package me.nickellis.caturday.ui.breeds

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.cat_images_fragment.*

import me.nickellis.caturday.R
import me.nickellis.caturday.injector
import me.nickellis.caturday.repo.cat.CatBreedsQuery
import me.nickellis.caturday.ui.BaseFragment
import me.nickellis.caturday.ui.common.list.CatBreedsPagedAdapter
import me.nickellis.caturday.ui.common.state.DataSourceState

class CatBreedsFragment : BaseFragment() {

  companion object {
    const val TAG = "CatBreedsFragment"
    fun newInstance() = CatBreedsFragment()
  }

  private lateinit var viewModel: CatBreedsViewModel
  private lateinit var breedsAdapter: CatBreedsPagedAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injector.inject(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_breeds_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    breedsAdapter = CatBreedsPagedAdapter()
    v_recycler.apply {
      adapter = breedsAdapter
      layoutManager = LinearLayoutManager(context)
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(CatBreedsViewModel::class.java)

    viewModel.catBreeds.observe(this, Observer(breedsAdapter::submitList))
    viewModel.networkState.observe(this, networkObserver)

    viewModel.setQuery(CatBreedsQuery(pageSize = 50))
  }

  private val networkObserver = Observer<DataSourceState> { state ->
    when (state) {
      is DataSourceState.LoadInitial, is DataSourceState.LoadAfter -> Log.d(TAG, "Loading!")
      is DataSourceState.Success -> Log.d(TAG, "Success!")
      is DataSourceState.Error -> Log.d(TAG, "Error!")
    }
  }

}
