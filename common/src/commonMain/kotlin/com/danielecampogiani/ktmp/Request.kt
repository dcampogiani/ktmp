package com.danielecampogiani.ktmp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class Request<T> {
    abstract suspend fun execute(): T

    fun executeCallback(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        GlobalScope.launch(ApplicationDispatcher) {

            try {
                val result = execute()
                onSuccess(result)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun <R> map(f: (T) -> R): Request<R> =
        MapRequest(request = this, map = f)
}

private class MapRequest<T, R>(
    private val request: Request<T>,
    private val map: (T) -> R
) : Request<R>() {

    override suspend fun execute(): R = map(request.execute())
}