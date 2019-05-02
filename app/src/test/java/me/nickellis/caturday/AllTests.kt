package me.nickellis.caturday

import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.nickellis.caturday.ui.CatBreedsViewModelTests
import me.nickellis.caturday.ui.CatImagesViewModelTests
import me.nickellis.caturday.ui.common.FragmentStackTests
import org.junit.runner.RunWith
import org.junit.runners.Suite


@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(CatImagesViewModelTests::class, CatBreedsViewModelTests::class, FragmentStackTests::class)
class AllTests