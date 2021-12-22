package com.nomean.rating.api.auth

import com.nomean.rating.api.auth.dto.UserInfoVo
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthDao {
    fun createNewUser(userId: Int, resource: String, token: String)
    fun setRefreshToken(userId: Int, refreshToken: String, expireDate: Date)
    fun getUserInfoByThirdParty(resource : String, token: String) : UserInfoVo?
    fun getUserInfoByRefreshToken(token: String) : UserInfoVo?
}