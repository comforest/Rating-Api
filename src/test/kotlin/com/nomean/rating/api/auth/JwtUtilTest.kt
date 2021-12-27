package com.nomean.rating.api.auth

import Configure
import com.nomean.rating.api.auth.dto.JwtPayloadVo
import com.nomean.rating.api.util.JwtUtil
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.longs.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import java.lang.System.currentTimeMillis
import java.security.Key
import java.util.*
import kotlin.math.abs
import kotlin.random.Random


class JwtUtilTest : Configure() {
    @Autowired
    private lateinit var jwt: JwtUtil

    @Value("\${jwt.key}")
    private val jwtKey: String = ""
    private val key: Key by lazy { Keys.hmacShaKeyFor(jwtKey.toByteArray()) }

    init {
        describe("JWT 생성 Test") {
            it("랜덤한 유저 Token 생성 (총 100)") {
                val random = Random(currentTimeMillis())
                for (i in 1..100) {
                    val authData = JwtPayloadVo(random.nextInt())
                    val expireTime = abs(random.nextInt() % 30 + 30) * 1000 * 60 * 60 * 24L
                    val token = jwt.createToken(authData, expireTime)

                    shouldNotThrow<JwtException> {
                        val parser = Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()

                        val jwt = parser.parseClaimsJws(token)
                        val body = jwt.body

                        val timeDiff = body.expiration.time - (currentTimeMillis() + expireTime)
                        abs(timeDiff) shouldBeLessThanOrEqual 2000
                        body["id"] shouldBe authData.id
                    }
                }
            }
        }
        describe("JWT 검증 Test") {
            context("Success") {
                val user = JwtPayloadVo(1)
                val token = createToken(key, user.id, 60 * 1000L)

                it("validateToken") {
                    jwt.validateToken(token) shouldBe true
                }

                it("extractPayload") {
                    jwt.extractPayload(token) shouldBe user
                }
            }
            context("ExpireDate Fail") {
                val user = JwtPayloadVo(1)
                val token = createToken(key, user.id, -1)

                it("validateToken") {
                    jwt.validateToken(token) shouldBe false
                }

                it("extractPayload") {
                    jwt.extractPayload(token) shouldBe null
                }
            }

            context("다른 키 사용 Fail") {
                val faultKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)

                val user = JwtPayloadVo(1)
                val token = createToken(faultKey, user.id, -1)

                it("validateToken") {
                    jwt.validateToken(token) shouldBe false
                }

                it("extractPayload") {
                    jwt.extractPayload(token) shouldBe null
                }
            }
        }
    }

    private fun createToken(key: Key, id: Int, time: Long): String {
        val expireDate = Date()
        expireDate.time += time

        return Jwts.builder()
            .setSubject("Test")
            .setExpiration(expireDate)
            .claim("id", id)
            .signWith(key)
            .compact()
    }

}