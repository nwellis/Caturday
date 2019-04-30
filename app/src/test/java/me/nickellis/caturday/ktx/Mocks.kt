package me.nickellis.caturday.ktx

import androidx.paging.PagedList
import me.nickellis.caturday.domain.common.AppError
import me.nickellis.caturday.mocks.MockRepositoryRequest
import me.nickellis.caturday.repo.RepositoryRequest
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*

inline fun <reified T> mock(): T = mock(T::class.java)

/**
 * Annoying I have to do this
 * @see [article](https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791)
 */
fun <T> anyKClass(): T {
  any<T>()
  return uninitialized()
}

@Suppress("UNCHECKED_CAST")
private fun <T> uninitialized(): T = null as T

fun <T> List<T>.toMockPagedList(): PagedList<T> {
  @Suppress("UNCHECKED_CAST")
  val pagedList = mock(PagedList::class.java) as PagedList<T>

  `when`(pagedList[ArgumentMatchers.anyInt()]).then { mockInvocation ->
    get(mockInvocation.arguments.first() as Int)
  }

  `when`(pagedList.isDetached).thenReturn(false)
  `when`(pagedList.size).thenReturn(size)

  return pagedList
}

fun <T> T.wrapWithMockRequest(): RepositoryRequest<T> = MockRepositoryRequest(this)

fun <T> AppError.wrapWithMockRequest(message: String? = null): RepositoryRequest<T>
    = MockRepositoryRequest(null, AppError(message ?: "${UUID.randomUUID()}: mock request error"))