package com.nomean.rating.api.auth.dto

data class AccessTokenVo(
    val accessToken: String,
    val refreshToken: String?
)
