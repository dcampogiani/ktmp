package com.danielecampogiani.ktmp

import com.danielecampogiani.ktmp.category.IntegrationTest
import com.danielecampogiani.ktmp.sample.Api
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.experimental.categories.Category


@Category(IntegrationTest::class)
class IntegrationResponseTest {

    private val api = Api()

    @Test
    fun mapSuccess() = runBlocking {
        val call = api.getStargazers("dcampogiani", "ktmp")
        val response = call.execute()
        val mappedResponse =
            response.map { list -> list.map { stargazer -> stargazer.copy(userName = stargazer.userName.toUpperCase()) } }

        mappedResponse.fold(
            { fail() },
            {
                assertEquals(200, it.code)
                assertEquals("GIOEVI90", it.body.first().userName)
            })
    }

    @Test
    fun mapError() = runBlocking {
        val call = api.getStargazers("dcampogiani", "NotValid")
        val response = call.execute()
        val mappedResponse = response.map { "Dummy Mapping" }

        mappedResponse.fold(
            { assertEquals(404, response.code) },
            { fail() })
    }

}
