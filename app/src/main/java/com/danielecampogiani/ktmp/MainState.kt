package com.danielecampogiani.ktmp

import com.danielecampogiani.ktmp.sample.Person

sealed class MainState {

    data class Result(val person: Person) : MainState()
    data class Error(val message: String) : MainState()
}