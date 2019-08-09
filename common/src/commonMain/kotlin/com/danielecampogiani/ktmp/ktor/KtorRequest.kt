package com.danielecampogiani.ktmp.ktor

import com.danielecampogiani.ktmp.Request
import com.danielecampogiani.ktmp.Response
import com.danielecampogiani.ktmp.Success
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.URLBuilder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

internal sealed class KtorRequest<T : Any>(
    open val client: HttpClient,
    open val uri: URLBuilder,
    private val serializer: KSerializer<T>
) : Request<T>() {

    internal class Get<T : Any>(
        client: HttpClient,
        uri: URLBuilder,
        serializer: KSerializer<T>
    ) : KtorRequest<T>(client, uri, serializer) {

        override suspend fun execute(): Response<T> {
            val httpResponse = client.get<HttpResponse> {
                url(uri.buildString())
            }
            val body = parseJson(httpResponse.readText())
            return Success(body, httpResponse.status.value)
        }
    }

    protected fun parseJson(result: String): T {
        return Json.nonstrict.parse(serializer, result)
    }
}