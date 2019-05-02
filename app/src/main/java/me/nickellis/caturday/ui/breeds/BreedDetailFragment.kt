package me.nickellis.caturday.ui.breeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.breed_detail_fragment.*

import me.nickellis.caturday.R
import me.nickellis.caturday.domain.CatBreed
import me.nickellis.caturday.injector
import me.nickellis.caturday.ktx.visible
import me.nickellis.caturday.ui.BaseFragment

class BreedDetailFragment : BaseFragment() {

  companion object {
    const val TAG = "BreedDetailFragment"
    fun newInstance() = BreedDetailFragment()
  }

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

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    sharedViewModel.selectedBreed.observe(this, breedObserver)
  }

  private val breedObserver = Observer<CatBreed> { breed ->
    breed.apply {
      v_name.text = name
      v_temperament.text = temperament
      v_description.text = description

      val blankToNullWikiUrl = wikipediaUrl?.takeIf { it.isNotBlank() }
      v_wikipedia_button.visible(blankToNullWikiUrl != null)
      blankToNullWikiUrl?.let { url ->
        v_wikipedia_button.setOnClickListener {

        }
      }

    }
  }

}
