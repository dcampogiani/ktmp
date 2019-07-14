package com.danielecampogiani.ktmp

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val results: List<Result>
)

@Serializable
data class Result(
    val name: Name,
    val email: String
)

@Serializable
data class Name(
    val first: String,
    val last: String
)