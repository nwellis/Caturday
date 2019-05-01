package me.nickellis.caturday.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.PagedList
import kotlinx.coroutines.*
import me.nickellis.caturday.InstantAppExecutors
import me.nickellis.caturday.domain.CatImage
import me.nickellis.caturday.domain.common.AppError
import me.nickellis.caturday.ktx.*
import me.nickellis.caturday.repo.cat.CatImagesQuery
import me.nickellis.caturday.repo.cat.CatRepository
import me.nickellis.caturday.ui.common.state.DataSourceState
import me.nickellis.caturday.ui.common.state.NetworkState
import me.nickellis.caturday.ui.search.SearchCatsViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
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
  }

  @Test
  fun `search for images success`() {
    // Arrange
    `when`(mockCatRepository.getCatImages(anyKClass()))
      .thenReturn(mockPages[0].wrapWithMockRequest())

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
    verify(mockCatRepository, times(1)).getCatImages(anyKClass())
    verify(imagesObserver, times(1)).onChanged(anyKClass())
    verify(networkObserver, times(1)).onChanged(DataSourceState.Success)
  }

  @Test
  fun `search for images error`() {
    // Arrange
    val error = AppError(message = "mock error")

    `when`(mockCatRepository.getCatImages(anyKClass()))
      .thenReturn(error.wrapErrorWithMockRequest())

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
    verify(mockCatRepository, times(1)).getCatImages(anyKClass())
    verify(imagesObserver, times(1)).onChanged(anyKClass())
    verify(networkObserver, times(1)).onChanged(DataSourceState.Error(error))
  }
}