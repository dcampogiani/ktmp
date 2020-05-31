package com.danielecampogiani.ktmp

import com.danielecampogiani.ktmp.category.UnitTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import org.junit.Test
import org.junit.experimental.categories.Category


@Category(UnitTest::class)
class UnitResponseTest {

    @Test
    fun zipBothSuccess() {
        val a = Response.just("42")
        val b = Response.just(2)

        val response = Response.zip(a, b) { first, second -> first.toInt() * second }

        response.fold(
            { fail() },
            {
                assertEquals(200, it.code)
                assertEquals(84, it.body)
            }
        )
    }

    @Test
    fun zipFirstError() {
        val a = Response.error<String>("firstError")
        val b = Response.just(42)

        val response = Response.zip(a, b) { first, second -> first.toUpperCase() + second }

        response.fold(
            {
                assertEquals("firstError", it.errorBody)
                assertEquals(500, it.code)
            },
            {
                fail()
            }
        )
    }

    @Test
    fun zipSecondError() {
        val a = Response.just(42)
        val b = Response.error<Int>("secondError")

        val response = Response.zip(a, b) { first, second -> first + second }

        response.fold(
            {
                assertEquals("secondError", it.errorBody)
                assertEquals(500, it.code)
            },
            {
                fail()
            }
        )
    }

    @Test
    fun zipBothError() {
        val a = Response.error<Int>("firstError")
        val b = Response.error<Int>("secondError")

        val response = Response.zip(a, b) { first, second -> first + second }

        response.fold(
            {
                assertEquals("firstError", it.errorBody)
                assertEquals(500, it.code)
            },
            {
                fail()
            }
        )
    }
}
