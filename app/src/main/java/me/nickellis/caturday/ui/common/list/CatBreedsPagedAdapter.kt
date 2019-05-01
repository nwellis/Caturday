package me.nickellis.caturday.ui.common.list

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import me.nickellis.caturday.domain.CatBreed
import me.nickellis.caturday.ui.common.list.viewholder.CatBreedViewHolder

class CatBreedsPagedAdapter: PagedListAdapter<CatBreed, CatBreedViewHolder>(diffCallback) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatBreedViewHolder {
    return CatBreedViewHolder(parent)
  }

  override fun onBindViewHolder(holder: CatBreedViewHolder, position: Int) {
    holder.bindTo(getItem(position))
  }

  companion object {
    private val diffCallback = object : DiffUtil.ItemCallback<CatBreed>() {
      override fun areItemsTheSame(oldItem: CatBreed, newItem: CatBreed): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(oldItem: CatBreed, newItem: CatBreed): Boolean {
        return oldItem == newItem
      }
    }
  }
}