package me.nickellis.caturday.ui.breeds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.breed_detail_fragment.*

import me.nickellis.caturday.R
import me.nickellis.caturday.domain.CatBreed
import me.nickellis.caturday.injector
import me.nickellis.caturday.ktx.startWebIntent
import me.nickellis.caturday.ktx.visible
import me.nickellis.caturday.ui.BaseFragment

class BreedDetailFragment : BaseFragment() {

  companion object {
    const val TAG = "BreedDetailFragment"

    /**
     * Creates a view to display information about a specific cat breed.
     *
     * @param breed the cat breed to display
     * @return a new instance of the [BreedDetailFragment]
     */
    fun newInstance(breed: CatBreed) = BreedDetailFragment().apply {
      arguments = Bundle().apply {
        putParcelable(ARG_BREED, breed)
      }
    }

    private const val ARG_BREED = "breedDetail_breed"
  }

  private lateinit var viewModel: BreedDetailViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injector.inject(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.breed_detail_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    viewModel = ViewModelProviders
      .of(this, viewModelFactory)
      .get(BreedDetailViewModel::class.java)

    if (savedInstanceState == null) {
      arguments?.apply {
        getParcelable<CatBreed>(ARG_BREED)
          ?.also { remove(ARG_BREED) }
          ?.let { viewModel.setBreed(it) }
          ?: throw IllegalStateException("This fragment must be constructed with newInstance")
      }
    }

    viewModel.getBreed().observe(viewLifecycleOwner, breedObserver)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    viewModel.saveTo(outState)
  }

  override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
    viewModel.restoreFrom(savedInstanceState)
  }

  private val breedObserver = Observer<CatBreed> { breed ->
    breed.apply {
      v_name.text = name
      v_temperament.text = temperament
      v_description.text = description

      val blankToNullWikiUrl = wikipediaUrl?.takeIf { it.isNotBlank() }

      v_wikipedia_button.apply {
        visible(blankToNullWikiUrl != null)
        blankToNullWikiUrl?.let { url ->
          setOnClickListener {
            activity?.startWebIntent(url) { ex ->
              Log.e(TAG, "Unable to start web intent for $url")
            }
          }
        }
      }
    }
  }

}
