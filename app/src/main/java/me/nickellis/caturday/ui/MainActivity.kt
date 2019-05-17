package me.nickellis.caturday.ui

import android.os.Bundle
import me.nickellis.caturday.R
import me.nickellis.caturday.ui.common.events.FragmentEvent
import me.nickellis.caturday.ui.common.events.FragmentObserver
import me.nickellis.caturday.ui.common.navigation.FragmentStack
import me.nickellis.caturday.ui.main.MainFragment

class MainActivity : BaseActivity(), FragmentObserver {

  private lateinit var navigation: FragmentStack

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
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
      is FragmentEvent.Back -> onBackPressed()
      is FragmentEvent.New -> navigation.push(event.fragment)
    }
  }
}
