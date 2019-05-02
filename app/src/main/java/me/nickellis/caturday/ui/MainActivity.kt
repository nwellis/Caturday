package me.nickellis.caturday.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import me.nickellis.caturday.R
import me.nickellis.caturday.injector
import me.nickellis.caturday.ui.breeds.BreedDetailFragment
import me.nickellis.caturday.ui.breeds.CatBreedsFragment
import me.nickellis.caturday.ui.common.events.FragmentBack
import me.nickellis.caturday.ui.common.events.FragmentEvent
import me.nickellis.caturday.ui.common.events.FragmentObserver
import me.nickellis.caturday.ui.common.events.NewBreedDetail
import me.nickellis.caturday.ui.common.navigation.FragmentStack
import me.nickellis.caturday.ui.images.CatImagesFragment
import me.nickellis.caturday.ui.main.MainFragment
import javax.inject.Inject

class MainActivity : BaseActivity(), SharedViewModelProvider, FragmentObserver {

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var navigation: FragmentStack

  override val sharedViewModel: SharedViewModel by lazy {
    ViewModelProviders.of(this, viewModelFactory).get(SharedViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injector.inject(this)
    setContentView(R.layout.activity_main)

    navigation = FragmentStack(
      manager = supportFragmentManager,
      containerViewId = R.id.v_container,
      inState = savedInstanceState
    )

    if (navigation.isEmpty) {
      navigation.push(MainFragment.newInstance())
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    navigation.save(outState)
    super.onSaveInstanceState(outState)
  }

  override fun onBackPressed() {
    navigation.pop()
    if (navigation.isEmpty) {
      super.onBackPressed()
    }
  }

  override fun onFragmentEvent(event: FragmentEvent) {
    when (event) {
      is FragmentBack -> onBackPressed()
      is NewBreedDetail -> {
        sharedViewModel.selectedBreed.postValue(event.breed)
        navigation.push(BreedDetailFragment.newInstance())
      }
    }
  }
}
