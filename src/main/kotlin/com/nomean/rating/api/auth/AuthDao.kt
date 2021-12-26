package com.nomean.rating.api.auth

import com.nomean.rating.api.auth.dto.ThirdPartyTokenVo
import com.nomean.rating.api.auth.dto.UserInfoVo
import java.util.*

interface AuthDao {
    fun createUser(token: ThirdPartyTokenVo)
    fun deleteUser(userId: Int)

    fun addRefreshToken(userId: Int, refreshToken: String, expireDate: Date)
    fun deleteRefreshToken(userId: Int, refreshToken: String)
    fun deleteExpiredRefreshToken()

    fun getUserInfoByThirdParty(token: ThirdPartyTokenVo) : UserInfoVo?
    fun getUserInfoByRefreshToken(token: String) : UserInfoVo?
}