package me.nickellis.caturday.ui

import android.os.Bundle
import android.os.PersistableBundle
import me.nickellis.caturday.R
import me.nickellis.caturday.injector
import me.nickellis.caturday.ui.breeds.CatBreedsFragment
import me.nickellis.caturday.ui.common.navigation.FragmentStack
import me.nickellis.caturday.ui.images.CatImagesFragment

class MainActivity : BaseActivity() {

  private lateinit var navigation: FragmentStack

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
      navigation.push(CatBreedsFragment.newInstance())
    }
  }

  override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
    navigation.save(outState)
    super.onSaveInstanceState(outState, outPersistentState)
  }
}
