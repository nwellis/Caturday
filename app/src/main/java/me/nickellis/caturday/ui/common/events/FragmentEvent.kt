package me.nickellis.caturday.ui.common.events

import me.nickellis.caturday.domain.CatBreed

interface FragmentObserver {
  fun onFragmentEvent(event: FragmentEvent)
}

sealed class FragmentEvent
object FragmentBack: FragmentEvent()

data class NewBreedDetail(val breed: CatBreed): FragmentEvent()