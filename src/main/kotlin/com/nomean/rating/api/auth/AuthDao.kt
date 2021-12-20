package com.nomean.rating.api.auth

import com.nomean.rating.api.auth.dto.UserAuthVO
import com.nomean.rating.api.auth.dto.UserInfoVo
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthDao {
    fun createNewUser(data : UserAuthVO)
    fun setRefreshToken(userId: Int, token: String, expireDate: Date)
    fun getUserInfoByThirdParty(resource : String, token: String) : UserInfoVo?
    fun getUserInfoByRefreshToken(token: String) : UserInfoVo?
}