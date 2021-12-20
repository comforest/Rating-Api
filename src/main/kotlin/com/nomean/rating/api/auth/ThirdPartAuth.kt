package com.nomean.rating.api.auth

import com.nomean.rating.api.auth.dto.UserInfoVo

interface ThirdPartAuth {
    fun validateLogin(token: String) : Boolean
    fun getUserInfo(token: String) : UserInfoVo?
}