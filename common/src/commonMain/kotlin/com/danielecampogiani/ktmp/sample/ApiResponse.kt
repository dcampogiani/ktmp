package com.danielecampogiani.ktmp.sample

import kotlinx.serialization.Serializable

@Serializable
internal data class ApiResponse(
    val results: List<Result>
)

@Serializable
internal data class Result(
    val name: Name,
    val email: String
)

@Serializable
internal data class Name(
    val first: String,
    val last: String
)