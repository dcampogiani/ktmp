package com.danielecampogiani.ktmp.sample

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("login") val login: String,
    @SerialName("followers_url") val followersUrl: String
)