package me.nickellis.caturday.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.fragment_main.*

import me.nickellis.caturday.R
import me.nickellis.caturday.ui.breeds.CatBreedsFragment
import me.nickellis.caturday.ui.images.CatImagesFragment

class MainFragment : Fragment() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_main, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    v_tab_layout.setupWithViewPager(v_view_pager)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    v_view_pager.adapter = CatImagesAndBreedsAdapter(childFragmentManager)
  }

  private data class FragmentInfo(
    val title: String,
    val creator: () -> Fragment
  )

  private inner class CatImagesAndBreedsAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    val fragments = listOf(
      FragmentInfo(resources.getString(R.string.btn_images)) { CatImagesFragment.newInstance() },
      FragmentInfo(resources.getString(R.string.btn_breeds)) { CatBreedsFragment.newInstance() }
    )

    override fun getItem(position: Int): Fragment {
      return fragments.getOrNull(position)?.creator?.invoke() ?: throw Exception("Illegal position @ $position")
    }

    override fun getPageTitle(position: Int): CharSequence? {
      return fragments.getOrNull(position)?.title
    }

    override fun getCount(): Int = fragments.size
  }

  companion object {
    @JvmStatic
    fun newInstance() = MainFragment()
  }
}
