package com.danielecampogiani.ktmp

import com.danielecampogiani.ktmp.category.IntegrationTest
import com.danielecampogiani.ktmp.sample.Api
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category


@Category(IntegrationTest::class)
class IntegrationRequestTest {

    private val api = Api()

    @Test
    fun flatMapSuccess() = runBlocking {
        val call = api.getUser("Donald90").flatMap {
            api.getFollowers(it.followersUrl)
        }
        val response = call.execute()

        response.fold(
            { fail() },
            { success ->
                val dcampogiani = success.body.find { user -> user.login == "dcampogiani" }
                assertEquals(200, success.code)
                assertNotNull(dcampogiani)
            })
    }

    @Test
    fun flatMapError() = runBlocking {
        val call = api.getUser("-1").flatMap {
            api.getFollowers(it.followersUrl)
        }
        val response = call.execute()

        response.fold(
            { assertEquals(404, response.code) },
            { fail() })
    }

    @Test
    fun zipBothSuccess() = runBlocking {
        val a = api.getUser("Donald90")
        val b = api.getUser("dcampogiani")

        val request = Request.zip(a, b) { donald90, dcampogiani ->
            "${donald90.login} ${dcampogiani.login}"
        }

        val response = request.execute()

        response.fold(
            { fail() },
            { success ->
                assertEquals(200, success.code)
                assertEquals("Donald90 dcampogiani", success.body)
            })
    }

    @Test
    fun zipFirstError() = runBlocking {
        val a = api.getUser("-1")
        val b = api.getUser("dcampogiani")

        val request = Request.zip(a, b) { error, dcampogiani ->
            "${error.login} ${dcampogiani.login}"
        }

        val response = request.execute()

        response.fold(
            {
                assertEquals(404, it.code)
                assertEquals(
                    "{\"message\":\"Not Found\",\"documentation_url\":\"https://developer.github.com/v3/users/#get-a-single-user\"}",
                    it.errorBody
                )
            },
            { fail() })
    }

    @Test
    fun zipSecondError() = runBlocking {
        val a = api.getUser("Donald90")
        val b = api.getUser("-1")

        val request = Request.zip(a, b) { donald90, error ->
            "${donald90.login} ${error.login}"
        }

        val response = request.execute()

        response.fold(
            {
                assertEquals(404, it.code)
                assertEquals(
                    "{\"message\":\"Not Found\",\"documentation_url\":\"https://developer.github.com/v3/users/#get-a-single-user\"}",
                    it.errorBody
                )
            },
            { fail() })
    }

    @Test
    fun zipBothError() = runBlocking {
        val a = api.getUser("-1")
        val b = api.getUser("-2")

        val request = Request.zip(a, b) { error1, error2 ->
            "${error1.login} ${error2.login}"
        }

        val response = request.execute()

        response.fold(
            {
                assertEquals(404, it.code)
                assertEquals(
                    "{\"message\":\"Not Found\",\"documentation_url\":\"https://developer.github.com/v3/users/#get-a-single-user\"}",
                    it.errorBody
                )
            },
            { fail() })
    }
}


