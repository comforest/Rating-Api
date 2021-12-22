package com.nomean.rating.api.auth

import com.nomean.rating.api.auth.dto.AccessTokenVo
import com.nomean.rating.api.auth.dto.ThirdPartyTokenVo
import com.nomean.rating.api.auth.dto.UserInfoVo


interface AuthService {
    fun login(tokenDto: ThirdPartyTokenVo): UserInfoVo
    fun withdraw(userId: Int)
    fun createAccessToken(refreshToken: String) : AccessTokenVo
}