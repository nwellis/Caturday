package me.nickellis.pokedex.repo

import androidx.lifecycle.Lifecycle

sealed class RepositoryError(val message: String)

sealed class RepositoryResponse<T>
class SuccessResponse<T>(val data: T): RepositoryResponse<T>()
class ErrorResponse<T>(val error: RepositoryError): RepositoryResponse<T>()

typealias RepositoryCallback<T> = (response: RepositoryResponse<T>) -> Unit
typealias OnCancelledCallback = (succeeded: Boolean) -> Unit

interface RepositoryRequest<T> {
    suspend fun await(): T
    fun enqueue(callback: RepositoryCallback<T>)
    fun enqueueWith(lifecycle: Lifecycle, callback: RepositoryCallback<T>)
    fun cancel(onCanceled: OnCancelledCallback? = null)

    val isExecuted: Boolean
    val isCancelled: Boolean
}