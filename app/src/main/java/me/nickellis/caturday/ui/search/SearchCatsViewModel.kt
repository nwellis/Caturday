package me.nickellis.caturday.ui.search

import me.nickellis.caturday.repo.cat.CatRepository
import me.nickellis.caturday.ui.common.viewmodel.BaseViewModel
import javax.inject.Inject


class SearchCatsViewModel @Inject constructor(
  catRepository: CatRepository
): BaseViewModel() {
}