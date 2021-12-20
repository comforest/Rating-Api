package com.nomean.rating.api.auth.dto

data class UserAuthVO(
    val id: Int = -1,
    val resource: String? = null,
    val thirdPartToken: String? = null,
    val refreshToken: String? = null,
)
