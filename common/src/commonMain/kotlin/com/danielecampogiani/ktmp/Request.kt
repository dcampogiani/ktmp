package com.danielecampogiani.ktmp

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

abstract class Request<T : Any> {
    abstract suspend fun execute(): Response<T>

    fun executeCallback(
        onSuccess: (Response<T>) -> Unit,
        onError: (Throwable) -> Unit
    ): Cancellation<T> = Cancellation(this, onSuccess, onError).also { it.start() }

    fun <R : Any> map(f: (T) -> R): Request<R> =
        MapRequest(request = this, f = f)

    fun <R : Any> flatMap(f: (T) -> Request<R>): Request<R> =
        FlatMapRequest(request = this, f = f)

    companion object {
        fun <T : Any, R : Any, O : Any> zip(
            a: Request<T>,
            b: Request<R>,
            zipFun: (T, R) -> O
        ): Request<O> = Zip2Request(a, b, zipFun)
    }
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

private class FlatMapRequest<T : Any, R : Any>(
    private val request: Request<T>,
    private val f: (T) -> Request<R>
) : Request<R>() {
    override suspend fun execute(): Response<R> {
        return when (val firstResponse = request.execute()) {
            is Success -> f(firstResponse.body).execute()
            is Error -> Error(firstResponse.errorBody, firstResponse.code)
        }
    }

}

private class Zip2Request<T : Any, R : Any, O : Any>(
    private val a: Request<T>,
    private val b: Request<R>,
    private val zipFun: (T, R) -> O
) : Request<O>() {
    override suspend fun execute(): Response<O> = coroutineScope {
        val aDeferred = async { a.execute() }
        val bDeferred = async { b.execute() }

        val aResponse = aDeferred.await()
        val bResponse = bDeferred.await()
        Response.zip(aResponse, bResponse, zipFun)
    }

}