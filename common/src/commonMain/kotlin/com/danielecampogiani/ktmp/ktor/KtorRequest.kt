package com.danielecampogiani.ktmp.ktor

import com.danielecampogiani.ktmp.Request
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.URLBuilder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

internal sealed class KtorRequest<T>(
    open val client: HttpClient,
    open val uri: URLBuilder,
    private val serializer: KSerializer<T>
) : Request<T>() {

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