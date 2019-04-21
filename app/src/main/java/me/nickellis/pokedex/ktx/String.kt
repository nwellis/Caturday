package me.nickellis.pokedex.ktx

import android.util.Log
import java.util.*

fun String?.ensureNonNullId(logOnNotUnique: String? = null): String {
  return if (this == null) {
    logOnNotUnique?.let { message -> Log.e("ktx#String", message) }
    UUID.randomUUID().toString()
  } else {
    this
  }
}