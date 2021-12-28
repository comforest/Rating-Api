package com.nomean.rating.api.auth

import com.nomean.rating.api.auth.dto.AccessTokenVo
import com.nomean.rating.api.auth.dto.ThirdPartyTokenVo
import com.nomean.rating.api.util.JwtUtil
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/auth")
class AuthController (private val service: AuthService, private val jwtUtil: JwtUtil) {

    @PostMapping("/login")
    fun login(@RequestBody thirdPartyTokenVo: ThirdPartyTokenVo): AccessTokenVo {
        //val userInfo = service.login(thirdPartyTokenVo)

        return AccessTokenVo("", "")
    }


//    @PostMapping("/login")
//    fun login(resource: String, token: String): AccessTokenVo {
//        println("$resource $token")
//        val user = service.login(resource, token)
//        val jwt = jwtUtil.createToken(UserAuthVO(user.id))
//        return AccessTokenVo(jwt)
//    }
}