package me.nickellis.caturday.ktx

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.size
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun <T : View> T.visible(visible: Boolean): T {
  visibility = if (visible) View.VISIBLE else View.GONE
  return this
}

fun <T : ViewGroup> T.children(): List<View> = (0 until this.childCount).map { index -> getChildAt(index) }

fun <T : ImageView> T.loadImageFromWeb(url: String?): T {
  Glide.with(this)
    .load(url)
    .transition(DrawableTransitionOptions.withCrossFade())
    .into(this)
  return this
}