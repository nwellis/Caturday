package me.nickellis.caturday.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import kotlinx.coroutines.*
import me.nickellis.caturday.InstantAppExecutors
import me.nickellis.caturday.domain.CatImage
import me.nickellis.caturday.ktx.anyKClass
import me.nickellis.caturday.ktx.mock
import me.nickellis.caturday.ktx.wrapWithMockRequest
import me.nickellis.caturday.repo.cat.CatImagesQuery
import me.nickellis.caturday.repo.cat.CatRepository
import me.nickellis.caturday.ui.common.state.DataSourceState
import me.nickellis.caturday.ui.search.SearchCatsViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchCatsViewModelTests {

  @JvmField @Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  private val pageSize = 25
  private val mockData = (0 until 1000).map { id -> CatImage(id = id.toString(), url = "$id") }
  private val mockPages = (0 until mockData.size step pageSize).map { cursor ->
    mockData.subList(cursor, cursor + pageSize)
  }

  @Mock private lateinit var mockCatRepository: CatRepository

  private lateinit var viewModel: SearchCatsViewModel

  @Before
  fun setUp() {
    val executors = InstantAppExecutors()
    viewModel = SearchCatsViewModel(mockCatRepository, executors)

    `when`(mockCatRepository.getCatImages(anyKClass()))
      .thenReturn(mockPages[0].wrapWithMockRequest())
      .thenReturn(mockPages[1].wrapWithMockRequest())
      .thenReturn(mockPages[2].wrapWithMockRequest())
      .thenReturn(mockPages[3].wrapWithMockRequest())
  }

  @Test
  fun `search for images`() {
    // Arrange
    val query = CatImagesQuery(pageSize = pageSize)

    val imagesObserver = mock<Observer<PagedList<CatImage>>>()
    val networkObserver = mock<Observer<DataSourceState>>()

    viewModel.apply {
      catImages.observeForever(imagesObserver)
      networkState.observeForever(networkObserver)
    }
    // Act
    viewModel.setQuery(query)

    // Assert
    verify(mockCatRepository).getCatImages(anyKClass())
  }

  suspend fun foo() {
    delay(50)
  }
}