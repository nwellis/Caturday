package me.nickellis.caturday.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import kotlinx.coroutines.*
import me.nickellis.caturday.InstantAppExecutors
import me.nickellis.caturday.domain.CatImage
import me.nickellis.caturday.domain.common.AppError
import me.nickellis.caturday.ktx.*
import me.nickellis.caturday.mocks.TestDataFactory
import me.nickellis.caturday.repository.cat.CatImageSize
import me.nickellis.caturday.repository.cat.CatImagesDataFactory
import me.nickellis.caturday.repository.cat.CatImagesQuery
import me.nickellis.caturday.repository.cat.CatRepository
import me.nickellis.caturday.ui.common.state.DataSourceState
import me.nickellis.caturday.ui.images.CatImagesViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.internal.util.reflection.FieldSetter
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CatImagesViewModelTests {

  @JvmField @Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  private val pageSize = 25
  private val mockData = (0 until 1000).map { id -> TestDataFactory.newCatImage(id.toString()) }
  private val mockPages = mockData.chunked(pageSize)

  @Mock private lateinit var mockCatRepository: CatRepository
  @Mock private lateinit var mockDataFactory: CatImagesDataFactory

  private lateinit var viewModel: CatImagesViewModel

  @Before
  fun setUp() {
    val executors = InstantAppExecutors()
    viewModel = CatImagesViewModel(mockCatRepository, executors)
  }

  @Test
  fun `same query does not execute again`() {
    // Arrange
    val query = CatImagesQuery(imageSize = CatImageSize.Medium)
    viewModel.setDataFactory(mockDataFactory, query)

    // Act
    viewModel.setQuery(query) // shouldn't invoke setQuery

    // Assert
    verify(mockDataFactory, times(0)).setQuery(anyKClass())
  }

  @Test
  fun `get cat images success`() {
    // Arrange
    `when`(mockCatRepository.getRandomCatImages(anyKClass()))
      .thenReturn(mockPages[0].wrapWithMockRequest())

    val query = CatImagesQuery(imageSize = CatImageSize.Max)

    val imagesObserver = mock<Observer<PagedList<CatImage>>>()
    val networkObserver = mock<Observer<DataSourceState>>()

    viewModel.apply {
      catImages.observeForever(imagesObserver)
      networkState.observeForever(networkObserver)
    }

    // Act
    viewModel.setQuery(query)

    // Assert

    // Twice, once for initial factory instantiation and another for changing the query
    verify(mockCatRepository, times(2)).getRandomCatImages(anyKClass())
    verify(imagesObserver, times(2)).onChanged(anyKClass())
    verify(networkObserver, times(2)).onChanged(DataSourceState.Success)
  }

  @Test
  fun `get cat images error`() {
    // Arrange
    val error = AppError(message = "mock error")

    `when`(mockCatRepository.getRandomCatImages(anyKClass()))
      .thenReturn(error.wrapErrorWithMockRequest())

    val query = CatImagesQuery(imageSize = CatImageSize.Max)

    val imagesObserver = mock<Observer<PagedList<CatImage>>>()
    val networkObserver = mock<Observer<DataSourceState>>()

    viewModel.apply {
      catImages.observeForever(imagesObserver)
      networkState.observeForever(networkObserver)
    }

    // Act
    viewModel.setQuery(query)

    // Assert

    // Twice, once for initial factory instantiation and another for changing the query
    verify(mockCatRepository, times(2)).getRandomCatImages(anyKClass())
    verify(imagesObserver, times(2)).onChanged(anyKClass())
    verify(networkObserver, times(2)).onChanged(DataSourceState.Error(error))
  }

  private fun CatImagesViewModel.setDataFactory(factory: CatImagesDataFactory, factoryQuery: CatImagesQuery) {
    FieldSetter.setField(this, javaClass.getDeclaredField("factory"), factory)
    `when`(factory.query).thenReturn(factoryQuery)
  }
}