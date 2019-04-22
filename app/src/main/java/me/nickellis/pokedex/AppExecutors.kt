package me.nickellis.pokedex

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Global instance for executors in the application. This should be favored instead of creating your own thread
 * pools to ensure efficient reuse.
 */
object AppExecutors {

  val diskIO: Executor = Executors.newSingleThreadExecutor()
  val networkIO: Executor = Executors.newFixedThreadPool(3)
  val mainThread: Executor = MainThreadExecutor()

  val ioDispatcher = networkIO.asCoroutineDispatcher()
  val mainDispatcher = Dispatchers.Main

  private class MainThreadExecutor : Executor {
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    override fun execute(command: Runnable) {
      mainThreadHandler.post(command)
    }
  }

}