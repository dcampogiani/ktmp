package com.danielecampogiani.ktmp

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.URLBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

interface Request<T> {
    suspend fun execute(): T
}

fun <T, R> Request<T>.map(f: (T) -> R): Request<R> = MapRequest(request = this, map = f)

fun <T> Request<T>.executeCallback(
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

internal sealed class KtorRequest<T>(
    open val client: HttpClient,
    open val uri: URLBuilder,
    private val serializer: KSerializer<T>
) : Request<T> {

    internal class Get<T>(
        client: HttpClient,
        uri: URLBuilder,
        serializer: KSerializer<T>
    ) : KtorRequest<T>(client, uri, serializer) {

        override suspend fun execute(): T {
            val stringResult = client.get<String> {
                url(uri.buildString())
            }
            return parseJson(stringResult)
        }
    }

    protected fun parseJson(result: String): T {
        return Json.nonstrict.parse(serializer, result)
    }
}

private class MapRequest<T, R>(
    private val request: Request<T>,
    private val map: (T) -> R
) : Request<R> {

    override suspend fun execute(): R = map(request.execute())
}