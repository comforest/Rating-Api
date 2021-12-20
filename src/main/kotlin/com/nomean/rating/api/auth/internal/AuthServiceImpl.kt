package com.nomean.rating.api.auth.internal

import com.nomean.rating.api.auth.AuthService
import com.nomean.rating.api.auth.ThirdPartAuth
import com.nomean.rating.api.auth.dto.AccessTokenVo
import com.nomean.rating.api.auth.dto.UserInfoVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl @Autowired constructor(
    private val auth: Map<String, ThirdPartAuth>
) : AuthService {

    override fun login(resource: String, token: String): UserInfoVo {
        val authManager = auth["${resource}Auth"] ?: throw java.lang.Exception()
        return authManager.getUserInfo(token) ?: throw java.lang.Exception()
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