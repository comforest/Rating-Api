package com.nomean.rating.api.auth.internal

import com.nomean.rating.api.RequestInvalidException
import com.nomean.rating.api.auth.AuthService
import com.nomean.rating.api.auth.ThirdPartAuth
import com.nomean.rating.api.auth.dto.AccessTokenVo
import com.nomean.rating.api.auth.dto.ThirdPartyTokenVo
import com.nomean.rating.api.auth.dto.UserInfoVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl @Autowired constructor(
    private val auth: Map<String, ThirdPartAuth>
) : AuthService {

    override fun login(tokenDto: ThirdPartyTokenVo): UserInfoVo {
        val authManager = auth["${tokenDto.resource}Auth"] ?: throw RequestInvalidException()
        return authManager.getUserInfo(tokenDto.token) ?: throw RequestInvalidException()
    }

    override fun withdraw(userId: Int) {
        throw Exception()
    }

    override fun createAccessToken(refreshToken: String): AccessTokenVo {
        throw Exception()
    }


    fun test(@Qualifier("NaverAuth") auth: ThirdPartAuth) {
        print(auth)
    }
}