/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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