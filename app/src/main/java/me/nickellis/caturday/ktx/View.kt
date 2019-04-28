package me.nickellis.caturday.ktx

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


fun <T : ImageView> T.loadImageFromWeb(url: String?): T {
  Glide.with(this)
    .load(url)
    .transition(DrawableTransitionOptions.withCrossFade())
    .into(this)
  return this
}