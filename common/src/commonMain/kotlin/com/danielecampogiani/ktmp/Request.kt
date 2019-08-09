package com.danielecampogiani.ktmp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class Request<T : Any> {
    abstract suspend fun execute(): Response<T>

    fun executeCallback(
        onSuccess: (Response<T>) -> Unit,
        onError: (Throwable) -> Unit
    ): Cancellation<T> = Cancellation(this, onSuccess, onError).also { it.start() }

    fun <R : Any> map(f: (T) -> R): Request<R> =
        MapRequest(request = this, f = f)
}

class Cancellation<T : Any> internal constructor(
    private val request: Request<T>,
    private var onSuccess: ((Response<T>) -> Unit)?,
    private var onError: ((Throwable) -> Unit)?
) {

    internal fun start() {
        GlobalScope.launch(ApplicationDispatcher) {

            try {
                val result = request.execute()
                onSuccess?.invoke(result)
            } catch (e: Exception) {
                onError?.invoke(e)
            }
        }
    }

    fun cancel() {
        onSuccess = null
        onError = null

    }
}

private class MapRequest<T : Any, R : Any>(
    private val request: Request<T>,
    private val f: (T) -> R
) : Request<R>() {

    override suspend fun execute(): Response<R> = request.execute().map(f)
}