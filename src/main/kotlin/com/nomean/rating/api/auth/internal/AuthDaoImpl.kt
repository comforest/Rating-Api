package com.nomean.rating.api.auth.internal

import com.nomean.rating.api.auth.AuthDao
import com.nomean.rating.api.auth.dto.ThirdPartyTokenVo
import com.nomean.rating.api.auth.dto.UserInfoVo
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AuthDaoImpl : AuthDao {
    override fun createUser(token: ThirdPartyTokenVo) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(userId: Int) {
        TODO("Not yet implemented")
    }

    override fun addRefreshToken(userId: Int, refreshToken: String, expireDate: Date) {
        TODO("Not yet implemented")
    }

    override fun deleteRefreshToken(userId: Int, refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun deleteExpiredRefreshToken() {
        TODO("Not yet implemented")
    }

    override fun getUserInfoByThirdParty(token: ThirdPartyTokenVo): UserInfoVo? {
        TODO("Not yet implemented")
    }

    override fun getUserInfoByRefreshToken(token: String): UserInfoVo? {
        TODO("Not yet implemented")
    }
}