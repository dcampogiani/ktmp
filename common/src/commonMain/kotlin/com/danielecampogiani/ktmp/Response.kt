package com.danielecampogiani.ktmp

sealed class Response<out A : Any> {
    abstract val code: Int

    fun <B : Any> fold(fe: (Error<A>) -> B, fs: (Success<A>) -> B): B {
        return when (this) {
            is Error -> fe(this)
            is Success -> fs(this)
        }
    }

    fun <B : Any> map(f: (A) -> B): Response<B> {
        return fold({ Error(it.errorBody, it.code) }, { Success(f(it.body), it.code) })
    }

    fun <B : Any> flatMap(f: (A) -> Response<B>): Response<B> = fold({ Error(it.errorBody, it.code) }, { f(it.body) })

    companion object {
        fun <A : Any> just(value: A): Response<A> {
            return Success(
                value,
                200
            )
        }
    }
}

data class Success<out A : Any> internal constructor(
    val body: A,
    override val code: Int
) : Response<A>()

data class Error<out Nothing : Any> internal constructor(
    val errorBody: String,
    override val code: Int
) : Response<Nothing>()