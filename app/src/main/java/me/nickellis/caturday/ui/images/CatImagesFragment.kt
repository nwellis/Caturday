package me.nickellis.caturday.ui.images

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.cat_images_fragment.*
import kotlinx.android.synthetic.main.try_again_widget.*
import me.nickellis.caturday.R
import me.nickellis.caturday.ktx.visible
import me.nickellis.caturday.repository.cat.CatImageSize
import me.nickellis.caturday.repository.cat.CatImagesQuery
import me.nickellis.caturday.ui.BaseFragment
import me.nickellis.caturday.ui.common.list.CatImagesPagedAdapter
import me.nickellis.caturday.ui.common.state.DataSourceState

class CatImagesFragment : BaseFragment() {

  companion object {
    const val TAG = "CatImagesFragment"

    /**
     * Creates a view to display an infinite scrolling list of cat images
     *
     * @return a new instance of the [CatImagesFragment]
     */
    fun newInstance() = CatImagesFragment()
  }

  private var viewModel: CatImagesViewModel? = null
  private lateinit var imagesAdapter: CatImagesPagedAdapter

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
    viewModel = ViewModelProviders
      .of(this, viewModelFactory)
      .get(CatImagesViewModel::class.java)
      .also { it.restoreFrom(savedInstanceState) }
      .apply {
        catImages.observe(viewLifecycleOwner, Observer(imagesAdapter::submitList))
        networkState.observe(viewLifecycleOwner, networkObserver)
        setQuery(CatImagesQuery(imageSize = CatImageSize.Small))
      }

    v_try_again_button.setOnClickListener {
      viewModel?.retryFailedCall()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    viewModel?.saveTo(outState)
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
