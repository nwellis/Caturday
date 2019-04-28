package me.nickellis.caturday.ui.common.list

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import me.nickellis.caturday.domain.CatImage
import me.nickellis.caturday.ui.common.list.viewholder.CatImageViewHolder

class CatImagesPagedAdapter: PagedListAdapter<CatImage, CatImageViewHolder>(diffCallback) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatImageViewHolder {
    return CatImageViewHolder(parent)
  }

  override fun onBindViewHolder(holder: CatImageViewHolder, position: Int) {
    holder.bindTo(getItem(position))
  }

  companion object {
    private val diffCallback = object : DiffUtil.ItemCallback<CatImage>() {
      override fun areItemsTheSame(oldItem: CatImage, newItem: CatImage): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(oldItem: CatImage, newItem: CatImage): Boolean {
        return oldItem == newItem
      }
    }
  }
}