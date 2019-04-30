package me.nickellis.caturday

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Global instance for executors in the application. This should be favored instead of creating your own thread
 * pools to ensure efficient reuse.
 *
 * @see [Example](https://github.com/googlesamples/android-architecture-components)
 */
@Suppress("CanBeParameter", "unused")
@Singleton
open class AppExecutors(
  val diskIO: Executor,
  val networkIO: Executor,
  val mainThread: Executor
) {

  @Inject
  constructor() : this(
    Executors.newSingleThreadExecutor(),
    Executors.newFixedThreadPool(3),
    MainThreadExecutor()
  )

  val ioDispatcher = networkIO.asCoroutineDispatcher()
  val mainDispatcher = Dispatchers.Main

  private class MainThreadExecutor : Executor {
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    override fun execute(command: Runnable) {
      mainThreadHandler.post(command)
    }
  }
}