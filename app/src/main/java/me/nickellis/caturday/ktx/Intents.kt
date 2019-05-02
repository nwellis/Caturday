package me.nickellis.caturday.ktx

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity


fun FragmentActivity.startWebIntent(url: String, onError: (ex: ActivityNotFoundException) -> Unit) {
  try {
    startActivity(
      Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
      }
    )
  } catch (ex: ActivityNotFoundException) {
    onError(ex)
  }
}