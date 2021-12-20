package com.nomean.rating.api.auth.internal

import com.nomean.rating.api.auth.ThirdPartAuth
import com.nomean.rating.api.auth.dto.UserInfoVo
import org.springframework.stereotype.Component


@Component
class KakaoAuth : ThirdPartAuth{
    override fun validateLogin(token: String): Boolean {
        return false
    }

    override fun getUserInfo(token: String): UserInfoVo {
        return UserInfoVo(0,"")
    }

}