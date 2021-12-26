package com.nomean.rating.api.auth.internal

import com.nomean.rating.api.auth.AuthDao
import com.nomean.rating.api.auth.dto.UserInfoVo
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AuthDaoImpl : AuthDao {
    override fun createNewUser(resource: String, token: String) {
        TODO("Not yet implemented")
    }

    override fun setRefreshToken(userId: Int, refreshToken: String, expireDate: Date) {
        TODO("Not yet implemented")
    }

    override fun getUserInfoByThirdParty(resource: String, token: String): UserInfoVo? {
        TODO("Not yet implemented")
    }

    override fun getUserInfoByRefreshToken(token: String): UserInfoVo? {
        TODO("Not yet implemented")
    }
}