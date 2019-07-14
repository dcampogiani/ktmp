package com.danielecampogiani.ktmp

sealed class MainState {

    data class Result(val person: Person) : MainState()
    data class Error(val message: String) : MainState()
}