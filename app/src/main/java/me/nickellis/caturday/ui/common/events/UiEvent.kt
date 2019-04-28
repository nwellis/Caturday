package me.nickellis.caturday.ui.common.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Ensures that an UI event is only consumed once. [LiveData] does a nice job of resuming state, but firing off an
 * event from this is unwanted. The first invocation of [consume] will yield the result, and any after that will
 * throw null.
 */
class UiEvent<T>(private val data: T) {
  var isConsumed: Boolean = false
    private set

  /**
   * Consumes the event. If the event has already been consumed then this will not return anything.
   */
  fun consume(): T? = when {
    isConsumed -> null
    else -> {
      isConsumed = true
      data
    }
  }

  fun peek(): T = data
}

class UiEventObserver<T>(private inline val onChanged: (T) -> Unit): Observer<UiEvent<T>> {
  override fun onChanged(t: UiEvent<T>?) {
    t?.consume()?.let(onChanged)
  }
}

fun <T> MutableLiveData<UiEvent<T>>.postUiEvent(data: T) = postValue(UiEvent(data))