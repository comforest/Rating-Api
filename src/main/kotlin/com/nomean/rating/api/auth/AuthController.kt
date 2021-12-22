package com.nomean.rating.api.auth

import com.nomean.rating.api.auth.dto.AccessTokenVo
import com.nomean.rating.api.auth.dto.UserAuthVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController (private val service: AuthService, private val jwtUtil: JwtUtil) {

    @PostMapping("/login")
    fun login(): AccessTokenVo {
        val user = service.login("naver", "TOKEN_NAVER")
        val jwt = jwtUtil.createToken(UserAuthVO(user.id))
        return AccessTokenVo(jwt)
    }


//    @PostMapping("/login")
//    fun login(resource: String, token: String): AccessTokenVo {
//        println("$resource $token")
//        val user = service.login(resource, token)
//        val jwt = jwtUtil.createToken(UserAuthVO(user.id))
//        return AccessTokenVo(jwt)
//    }
}