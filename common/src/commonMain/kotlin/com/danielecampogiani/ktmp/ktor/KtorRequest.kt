package com.danielecampogiani.ktmp.ktor

import com.danielecampogiani.ktmp.Error
import com.danielecampogiani.ktmp.Request
import com.danielecampogiani.ktmp.Response
import com.danielecampogiani.ktmp.Success
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.response.readText
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
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
            return try {

                val httpResponse = client.get<HttpResponse> {
                    url(uri.buildString())
                }
                val body = parseJson(httpResponse.readText())
                Success(body, httpResponse.status.value)

            } catch (e: ClientRequestException) {
                val response = e.response
                Error(response.readText(), response.status.value)
            }
        }
    }

    protected fun parseJson(result: String): T {
        return Json.nonstrict.parse(serializer, result)
    }
}