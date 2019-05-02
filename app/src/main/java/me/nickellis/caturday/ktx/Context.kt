package me.nickellis.caturday.ktx

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources

val Resources.isPortrait get() = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
val Context.isPortrait get() = resources.isPortrait