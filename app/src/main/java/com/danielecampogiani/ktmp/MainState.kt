package com.danielecampogiani.ktmp

import com.danielecampogiani.ktmp.sample.Person
import com.danielecampogiani.ktmp.sample.User

sealed class MainState {

    data class Result(val person: List<User>) : MainState()
    data class Error(val message: String) : MainState()
}