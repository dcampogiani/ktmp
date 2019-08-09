package com.danielecampogiani.ktmp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class Request<T> {
    abstract suspend fun execute(): T

    fun executeCallback(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ): Cancellation<T> = Cancellation(this, onSuccess, onError).also { it.start() }

    fun <R> map(f: (T) -> R): Request<R> =
        MapRequest(request = this, map = f)
}

class Cancellation<T> internal constructor(
    private val request: Request<T>,
    private var onSuccess: ((T) -> Unit)?,
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

private class MapRequest<T, R>(
    private val request: Request<T>,
    private val map: (T) -> R
) : Request<R>() {

    override suspend fun execute(): R = map(request.execute())
}