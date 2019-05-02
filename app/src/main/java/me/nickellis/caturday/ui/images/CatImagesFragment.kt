package me.nickellis.caturday.ui.images

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.cat_images_fragment.*

import me.nickellis.caturday.R
import me.nickellis.caturday.injector
import me.nickellis.caturday.repository.cat.CatImageSize
import me.nickellis.caturday.repository.cat.CatImagesQuery
import me.nickellis.caturday.ui.BaseFragment
import me.nickellis.caturday.ui.common.list.CatImagesPagedAdapter
import me.nickellis.caturday.ui.common.state.DataSourceState

class CatImagesFragment : BaseFragment() {

  companion object {
    const val TAG = "CatImagesFragment"
    fun newInstance() = CatImagesFragment()
  }

  private lateinit var viewModel: CatImagesViewModel
  private lateinit var imagesAdapter: CatImagesPagedAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injector.inject(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_images_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    imagesAdapter = CatImagesPagedAdapter()
    v_recycler.apply {
      adapter = imagesAdapter
      //layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) If I knew the image size ahead of time I could do this easily
      layoutManager = GridLayoutManager(context, 2)
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(CatImagesViewModel::class.java)

    viewModel.catImages.observe(this, Observer(imagesAdapter::submitList))
    viewModel.networkState.observe(this, networkObserver)

    viewModel.setQuery(CatImagesQuery(imageSize = CatImageSize.Small, pageSize = 50))
  }

  private val networkObserver = Observer<DataSourceState> { state ->
    when (state) {
      is DataSourceState.LoadInitial, is DataSourceState.LoadAfter -> Log.d(TAG, "Loading!")
      is DataSourceState.Success -> Log.d(TAG, "Success!")
      is DataSourceState.Error -> Log.d(TAG, "Error!")
    }
  }

}
