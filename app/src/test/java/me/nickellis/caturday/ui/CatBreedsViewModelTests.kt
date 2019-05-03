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
  private val mockPages = mockData.chunked(pageSize)

  @Mock private lateinit var mockCatRepository: CatRepository
  @Mock private lateinit var mockDataFactory: CatBreedsDataFactory

  private lateinit var viewModel: CatBreedsViewModel

  @Before
  fun setUp() {
    val executors = InstantAppExecutors()
    viewModel = CatBreedsViewModel(mockCatRepository, executors)
  }

  @Test
  fun `same query does not execute again`() {
    // Arrange
    val query = CatBreedsQuery()
    viewModel.setDataFactory(mockDataFactory, query)

    // Act
    viewModel.getCatBreeds(query)

    // Assert
    verify(mockDataFactory, times(0)).setQuery(anyKClass())
  }

  @Test
  fun `get cat images success`() {
    // Arrange
    val query = CatBreedsQuery(pageSize = 25)

    `when`(mockCatRepository.getCatBreeds(anyKClass()))
      .thenReturn(mockPages[0].wrapWithMockRequest())

    val imagesObserver = mock<Observer<PagedList<CatBreed>>>()
    val networkObserver = mock<Observer<DataSourceState>>()

    viewModel.apply {
      catBreeds.observeForever(imagesObserver)
      networkState.observeForever(networkObserver)
    }

    // Act
    viewModel.getCatBreeds(query)

    // Assert

    // Twice, once for initial factory instantiation and another for changing the query
    verify(mockCatRepository, times(2)).getCatBreeds(anyKClass())
    verify(imagesObserver, times(2)).onChanged(anyKClass())
    verify(networkObserver, times(2)).onChanged(DataSourceState.Success)
  }

  @Test
  fun `get cat images error`() {
    // Arrange
    val error = AppError(message = "mock error")
    val query = CatBreedsQuery(pageSize = 25)

    `when`(mockCatRepository.getCatBreeds(anyKClass()))
      .thenReturn(error.wrapErrorWithMockRequest())

    val imagesObserver = mock<Observer<PagedList<CatBreed>>>()
    val networkObserver = mock<Observer<DataSourceState>>()

    viewModel.apply {
      catBreeds.observeForever(imagesObserver)
      networkState.observeForever(networkObserver)
    }

    // Act
    viewModel.getCatBreeds(query)

    // Assert

    // Twice, once for initial factory instantiation and another for changing the query
    verify(mockCatRepository, times(2)).getCatBreeds(anyKClass())
    verify(imagesObserver, times(2)).onChanged(anyKClass())
    verify(networkObserver, times(2)).onChanged(DataSourceState.Error(error))
  }

  private fun CatBreedsViewModel.setDataFactory(factory: CatBreedsDataFactory, factoryQuery: CatBreedsQuery) {
    FieldSetter.setField(this, javaClass.getDeclaredField("factory"), factory)
    `when`(factory.query).thenReturn(factoryQuery)
  }
}