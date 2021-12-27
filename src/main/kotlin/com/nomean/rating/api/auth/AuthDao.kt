package com.nomean.rating.api.auth

import com.nomean.rating.api.auth.dto.ThirdPartyTokenVo
import com.nomean.rating.api.auth.dto.UserInfoVo
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import java.util.*

@Mapper
interface AuthDao {
    fun createUser(token: ThirdPartyTokenVo)
    fun deleteUser(@Param("userId") userId: Int)

    fun addRefreshToken(
        @Param("userId") userId: Int,
        @Param("refreshToken") refreshToken: String,
        @Param("expireDate") expireDate: Date
    )

    fun deleteRefreshToken(@Param("userId") userId: Int, @Param("refreshToken") refreshToken: String)
    fun deleteExpiredRefreshToken()

    fun getUserInfoByThirdParty(token: ThirdPartyTokenVo): UserInfoVo?
    fun getUserInfoByRefreshToken(@Param("token") token: String): UserInfoVo?
}