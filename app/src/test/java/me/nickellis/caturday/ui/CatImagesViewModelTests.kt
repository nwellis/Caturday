package me.nickellis.caturday.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import kotlinx.coroutines.*
import me.nickellis.caturday.InstantAppExecutors
import me.nickellis.caturday.domain.CatImage
import me.nickellis.caturday.domain.common.AppError
import me.nickellis.caturday.ktx.*
import me.nickellis.caturday.repo.cat.CatImagesQuery
import me.nickellis.caturday.repo.cat.CatRepository
import me.nickellis.caturday.ui.common.state.DataSourceState
import me.nickellis.caturday.ui.images.CatImagesViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CatImagesViewModelTests {

  @JvmField @Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  private val pageSize = 25
  private val mockData = (0 until 1000).map { id -> CatImage(id = id.toString(), url = "$id", breeds = emptyList()) }
  private val mockPages = (0 until mockData.size step pageSize).map { cursor ->
    mockData.subList(cursor, cursor + pageSize)
  }

  @Mock private lateinit var mockCatRepository: CatRepository

  private lateinit var viewModel: CatImagesViewModel

  @Before
  fun setUp() {
    val executors = InstantAppExecutors()
    viewModel = CatImagesViewModel(mockCatRepository, executors)
  }

  @Test
  fun `get cat images success`() {
    // Arrange
    `when`(mockCatRepository.getRandomCatImages(anyKClass()))
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
    verify(mockCatRepository, times(1)).getRandomCatImages(anyKClass())
    verify(imagesObserver, times(1)).onChanged(anyKClass())
    verify(networkObserver, times(1)).onChanged(DataSourceState.Success)
  }

  @Test
  fun `get cat images error`() {
    // Arrange
    val error = AppError(message = "mock error")

    `when`(mockCatRepository.getRandomCatImages(anyKClass()))
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
    verify(mockCatRepository, times(1)).getRandomCatImages(anyKClass())
    verify(imagesObserver, times(1)).onChanged(anyKClass())
    verify(networkObserver, times(1)).onChanged(DataSourceState.Error(error))
  }
}