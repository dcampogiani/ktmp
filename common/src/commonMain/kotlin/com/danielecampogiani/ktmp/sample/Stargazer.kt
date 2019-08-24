package com.danielecampogiani.ktmp.sample

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stargazer(
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("login") val userName: String
)