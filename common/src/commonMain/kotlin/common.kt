package com.danielecampogiani.ktmp

import kotlinx.coroutines.CoroutineDispatcher

expect fun platformName(): String

fun createApplicationScreenMessage(): String {
    return "Kotlin Rocks on ${platformName()}"
}

internal expect val ApplicationDispatcher: CoroutineDispatcher