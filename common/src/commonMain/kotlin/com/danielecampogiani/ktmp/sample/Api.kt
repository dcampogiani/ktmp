package com.danielecampogiani.ktmp.sample

import com.danielecampogiani.ktmp.Request
import com.danielecampogiani.ktmp.ktor.KtorRequest
import io.ktor.client.HttpClient
import io.ktor.http.URLBuilder
import kotlinx.serialization.list

class Api {

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

    fun getStargazers(owner: String, repo: String): Request<List<Stargazer>> {
        return KtorRequest.Get(
            httpClient,
            URLBuilder("https://api.github.com/repos/$owner/$repo/stargazers"),
            Stargazer.serializer().list
        )
    }

    fun getUser(userName: String): Request<User> {
        return KtorRequest.Get(
            httpClient,
            URLBuilder("https://api.github.com/users/$userName"),
            User.serializer()
        )
    }

    fun getFollowers(url: String): Request<List<User>> {
        return KtorRequest.Get(
            httpClient,
            URLBuilder(url),
            User.serializer().list
        )
    }
}