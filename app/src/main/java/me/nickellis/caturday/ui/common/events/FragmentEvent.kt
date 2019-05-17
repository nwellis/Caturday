package me.nickellis.caturday.ui.common.events

import androidx.fragment.app.Fragment

interface FragmentObserver {
  fun onFragmentEvent(event: FragmentEvent)
}

sealed class FragmentEvent {
  object Back: FragmentEvent()
  class New(val fragment: Fragment): FragmentEvent()
}