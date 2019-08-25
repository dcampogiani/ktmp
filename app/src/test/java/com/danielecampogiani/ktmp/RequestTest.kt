package com.danielecampogiani.ktmp

import com.danielecampogiani.ktmp.category.IntegrationTest
import com.danielecampogiani.ktmp.sample.Api
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.experimental.categories.Category


@Category(IntegrationTest::class)
class RequestTest {

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

}
