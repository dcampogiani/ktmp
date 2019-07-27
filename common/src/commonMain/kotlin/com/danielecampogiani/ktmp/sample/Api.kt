package com.danielecampogiani.ktmp.sample

import com.danielecampogiani.ktmp.Request
import com.danielecampogiani.ktmp.ktor.KtorRequest
import io.ktor.client.HttpClient
import io.ktor.http.URLBuilder

public class Api() {

    private val httpClient = HttpClient()


    fun getRandomPerson(): Request<Person> {
        val rawRequest = KtorRequest.Get(
            httpClient,
            URLBuilder("https://randomuser.me/api/"),
            ApiResponse.serializer()
        )
        return rawRequest.map {
            val firstPerson = it.results.first()
            val name = firstPerson.name
            Person(name.first, name.last, firstPerson.email)
        }
    }
}