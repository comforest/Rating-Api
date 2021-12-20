package com.nomean.rating.api.auth

import com.nomean.rating.api.auth.dto.AccessTokenVo
import com.nomean.rating.api.auth.dto.UserAuthVO
import com.nomean.rating.api.auth.dto.UserInfoVo
import org.springframework.stereotype.Service


interface AuthService {
    fun login(resource: String, token: String): UserInfoVo
    fun withdraw(userId: Int)
    fun createAccessToken(refreshToken: String) : AccessTokenVo
}