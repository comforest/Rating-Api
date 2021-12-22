package com.nomean.rating.api.util

import com.nomean.rating.api.auth.dto.JwtPayloadVo
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtil {

    @Value("\${jwt.key}")
    private val jwtKey: String = ""

    private val key: Key by lazy {
        Keys.hmacShaKeyFor(jwtKey.toByteArray())
    }



    fun createToken(data: JwtPayloadVo, time: Long = 3600000): String {
        val expireDate = Date()
        expireDate.time += time

        return Jwts.builder()
            .setSubject("Test")
            .setExpiration(expireDate)
            .claim("id", data.id)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        try {
            readToken(token)
            return true
        } catch (e: JwtException) {
            return false
        }
    }

    fun extractPayload(token: String): JwtPayloadVo? {
        return try {
            val jwt = readToken(token)
            val payload = jwt.body

            JwtPayloadVo(payload["id"] as Int)
        } catch (e: JwtException) {
            null
        } catch (e: ClassCastException) {
            null
        }
    }

    private fun readToken(token: String): Jws<Claims> {
        val parser = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()

        return parser.parseClaimsJws(token)
    }
}