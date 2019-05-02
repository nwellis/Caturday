package me.nickellis.caturday.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import kotlinx.coroutines.*
import me.nickellis.caturday.InstantAppExecutors
import me.nickellis.caturday.domain.CatBreed
import me.nickellis.caturday.domain.common.AppError
import me.nickellis.caturday.ktx.*
import me.nickellis.caturday.mocks.TestDataFactory
import me.nickellis.caturday.repository.cat.CatBreedsDataFactory
import me.nickellis.caturday.repository.cat.CatBreedsQuery
import me.nickellis.caturday.repository.cat.CatRepository
import me.nickellis.caturday.ui.breeds.CatBreedsViewModel
import me.nickellis.caturday.ui.common.state.DataSourceState
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
class CatBreedsViewModelTests {

  @JvmField @Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  private val pageSize = 25
  private val mockData = (0 until 1000).map { id -> TestDataFactory.newCatBreed(id.toString()) }
  private val mockPages = (0 until mockData.size step pageSize).map { cursor ->
    mockData.subList(cursor, cursor + pageSize)
  }

  @Mock private lateinit var mockCatRepository: CatRepository
  @Mock private lateinit var mockDataFactory: CatBreedsDataFactory

  private lateinit var viewModel: CatBreedsViewModel

  @Before
  fun setUp() {
    val executors = InstantAppExecutors()
    viewModel = CatBreedsViewModel(mockCatRepository, executors)
  }

  @Test
  fun `same query executes only once`() {
    // Arrange
    viewModel.setDataFactory(mockDataFactory)

    // Act
    viewModel.getCatBreeds()
    viewModel.getCatBreeds()

    // Assert
    verify(mockDataFactory, times(1)).setQuery(anyKClass())
  }

  @Test
  fun `get cat images success`() {
    // Arrange
    `when`(mockCatRepository.getCatBreeds(anyKClass()))
      .thenReturn(mockPages[0].wrapWithMockRequest())

    val query = CatBreedsQuery(pageSize = pageSize)

    val imagesObserver = mock<Observer<PagedList<CatBreed>>>()
    val networkObserver = mock<Observer<DataSourceState>>()

    viewModel.apply {
      catBreeds.observeForever(imagesObserver)
      networkState.observeForever(networkObserver)
    }

    // Act
    viewModel.getCatBreeds()

    // Assert
    verify(mockCatRepository, times(1)).getCatBreeds(anyKClass())
    verify(imagesObserver, times(1)).onChanged(anyKClass())
    verify(networkObserver, times(1)).onChanged(DataSourceState.Success)
  }

  @Test
  fun `get cat images error`() {
    // Arrange
    val error = AppError(message = "mock error")

    `when`(mockCatRepository.getCatBreeds(anyKClass()))
      .thenReturn(error.wrapErrorWithMockRequest())

    val query = CatBreedsQuery(pageSize = pageSize)

    val imagesObserver = mock<Observer<PagedList<CatBreed>>>()
    val networkObserver = mock<Observer<DataSourceState>>()

    viewModel.apply {
      catBreeds.observeForever(imagesObserver)
      networkState.observeForever(networkObserver)
    }

    // Act
    viewModel.getCatBreeds()

    // Assert
    verify(mockCatRepository, times(1)).getCatBreeds(anyKClass())
    verify(imagesObserver, times(1)).onChanged(anyKClass())
    verify(networkObserver, times(1)).onChanged(DataSourceState.Error(error))
  }

  private fun CatBreedsViewModel.setDataFactory(factory: CatBreedsDataFactory) {
    FieldSetter.setField(this, javaClass.getDeclaredField("factory"), factory)
  }
}