package com.nomean.rating.api.auth.dto

import com.fasterxml.jackson.annotation.JsonInclude

//@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccessTokenVo(
    val accessToken: String,
    val refreshToken: String? = null
)
