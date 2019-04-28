package me.nickellis.caturday.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.nickellis.caturday.R
import me.nickellis.caturday.injector
import me.nickellis.caturday.repo.ErrorResponse
import me.nickellis.caturday.repo.SuccessResponse
import me.nickellis.caturday.repo.cat.CatRepository
import me.nickellis.caturday.repo.cat.CatImagesQuery
import me.nickellis.caturday.ui.common.navigation.FragmentStack
import me.nickellis.caturday.ui.search.SearchCatsFragment
import javax.inject.Inject

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
      navigation.push(SearchCatsFragment.newInstance())
    }
  }

  override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
    navigation.save(outState)
    super.onSaveInstanceState(outState, outPersistentState)
  }
}
