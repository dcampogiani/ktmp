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

    fun <B : Any> flatMap(f: (A) -> Response<B>): Response<B> =
        fold({ Error(it.errorBody, it.code) }, { f(it.body) })

    companion object {
        fun <A : Any> just(value: A): Response<A> {
            return Success(
                value,
                200
            )
        }

        fun <A : Any> error(errorBody: String): Response<A> {
            return Error(
                errorBody,
                500
            )
        }

        fun <T : Any, R : Any, O : Any> zip(
            a: Response<T>,
            b: Response<R>,
            zipFun: (T, R) -> O
        ): Response<O> {
            return a.fold(fe = { errorA ->
                Error(errorA.errorBody, errorA.code)
            }, fs = { successA ->
                b.fold(
                    fe = { errorB ->
                        Error(errorB.errorBody, errorB.code)
                    },
                    fs = { successB ->
                        Success(zipFun(successA.body, successB.body), successA.code)
                    }
                )
            })
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