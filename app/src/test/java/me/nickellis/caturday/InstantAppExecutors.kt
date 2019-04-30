package me.nickellis.caturday

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import java.util.concurrent.Executor


@ExperimentalCoroutinesApi
class InstantAppExecutors : AppExecutors(instant, instant, instant) {

  init {
    Dispatchers.setMain(instant.asCoroutineDispatcher())
  }

  companion object {
    val instant = Executor { it.run() }
  }
}