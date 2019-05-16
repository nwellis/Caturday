package me.nickellis.caturday.ui.common.list.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.nickellis.caturday.R
import me.nickellis.caturday.domain.CatImage
import me.nickellis.caturday.ktx.loadImageFromWeb


class CatImageViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
  LayoutInflater.from(parent.context).inflate(R.layout.cat_image_item, parent, false)
) {
  private val imageView = itemView.findViewById<ImageView>(R.id.v_image)
  private val headerView = itemView.findViewById<TextView>(R.id.v_header)

  fun bindTo(image: CatImage?) {
    imageView.loadImageFromWeb(image?.url)
  }
}