package me.nickellis.pokedex

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

/**
 * Global instance for executors in the application. This should be favored instead of creating your own thread
 * pools to ensure efficient reuse.
 *
 * @see [Example](https://github.com/googlesamples/android-architecture-components)
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

/**
 *
 */
suspend fun <T> withIOContext(
  block: suspend CoroutineScope.() -> T
): T = withContext(context = AppExecutors.ioDispatcher, block = block)