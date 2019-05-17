package me.nickellis.caturday.ui.breeds

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.cat_images_fragment.*
import kotlinx.android.synthetic.main.try_again_widget.*

import me.nickellis.caturday.R
import me.nickellis.caturday.injector
import me.nickellis.caturday.ktx.visible
import me.nickellis.caturday.repository.cat.CatBreedsQuery
import me.nickellis.caturday.ui.BaseFragment
import me.nickellis.caturday.ui.common.events.FragmentEvent
import me.nickellis.caturday.ui.common.list.BreedSelected
import me.nickellis.caturday.ui.common.list.CatBreedsPagedAdapter
import me.nickellis.caturday.ui.common.state.DataSourceState

class CatBreedsFragment : BaseFragment() {

  companion object {
    const val TAG = "CatBreedsFragment"

    /**
     * Creates a view to display an infinite scrolling list of cat breeds
     *
     * @return a new instance of the [CatBreedsFragment]
     */
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
      .onEvent { adapterEvent ->
        when (adapterEvent) {
          is BreedSelected -> {
            fragmentObserver.onFragmentEvent(
              FragmentEvent.New(BreedDetailFragment.newInstance(adapterEvent.breed))
            )
          }
        }
      }

    v_recycler.apply {
      adapter = breedsAdapter
      layoutManager = LinearLayoutManager(context)
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(CatBreedsViewModel::class.java)

    viewModel.catBreeds.observe(viewLifecycleOwner, Observer(breedsAdapter::submitList))
    viewModel.networkState.observe(viewLifecycleOwner, networkObserver)

    viewModel.getCatBreeds(CatBreedsQuery())

    v_try_again_button.setOnClickListener {
      viewModel.retryFailedCall()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    viewModel.saveTo(outState)
  }

  override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
    viewModel.restoreFrom(savedInstanceState)
  }

  private val networkObserver = Observer<DataSourceState> { state ->
    v_progress_indicator.visible(state is DataSourceState.LoadInitial)
    v_try_again.visible(state is DataSourceState.Error)
    when (state) {
      is DataSourceState.LoadInitial, is DataSourceState.LoadAfter -> Log.d(TAG, "Loading!")
      is DataSourceState.Success -> Log.d(TAG, "Success!")
      is DataSourceState.Error -> Log.d(TAG, "Error!")
    }
  }

}
