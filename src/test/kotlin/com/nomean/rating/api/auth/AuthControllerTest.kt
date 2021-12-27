package com.nomean.rating.api.auth

import DocConfigure
import DocSnippet
import com.nomean.rating.api.RequestInvalidException
import com.nomean.rating.api.auth.dto.ThirdPartyTokenVo
import com.nomean.rating.api.auth.dto.UserInfoVo
import com.nomean.rating.api.util.JsonUtil
import com.nomean.rating.api.util.JwtUtil
import io.kotest.assertions.any
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.Spec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class AuthControllerTest : DocConfigure() {

    @Autowired
    @InjectMockKs(injectImmutable = true, overrideValues = true)
    private lateinit var controller: AuthController

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    private val service: AuthService = mockk(relaxed = true)

    private val tokenNaver = ThirdPartyTokenVo("naver", "TOKEN_NAVER")
    private val tokenKakao = ThirdPartyTokenVo("kakao", "TOKEN_KAKAO")

    private val userNaver = UserInfoVo(1, "naver user")
    private val userKakao = UserInfoVo(2, "kakao user")

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)


        every { service.login(any()) } throws RequestInvalidException()
        every { service.login(tokenNaver) } returns userNaver
        every { service.login(tokenKakao) } returns userKakao
    }
    private val dataList = mutableListOf<Pair<ThirdPartyTokenVo, UserInfoVo>>()

    init {
        describe("Login Test") {

            forAll(
                row(tokenNaver, userNaver),
                row(tokenKakao, userKakao)
            ) { token, userInfo ->

                context(token.resource) {
                    it("Success") {
                        val jwt = controller.login(token)
                        val payload = jwtUtil.extractPayload(jwt.accessToken)

                        payload shouldNotBe null
                        payload!!.id shouldBe userInfo.id

                        jwt.refreshToken shouldNotBe null
                    }

                    it("Fail") {
                        shouldThrow<RequestInvalidException> {
                            controller.login(ThirdPartyTokenVo(token.resource, "${token.token}-F"))
                        }
                    }
                }
            }

            it("Doc") {
                mockMvc
                    .perform(
                        post("/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.convertToJson(tokenNaver))
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk)
                    .andDo(
                        document(
                            "com/nomean/rating/api/auth/login",
                            DocSnippet("Login", "서드파티를 이용한 로그인"),
                            requestFields(
                                fieldWithPath("resource").type(JsonFieldType.STRING)
                                    .description("서드파티 제공자 구분 +\n naver|kakao"),
                                fieldWithPath("token").type(JsonFieldType.STRING).description("서드파티에서 주어진 Access Token")
                            ),
                            responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                    .description("API 서버에서 사용하는 JWT 토큰"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                    .description("JWT 재발급 받을 수 있는 Refresh Token"),
                            )
                        )
                    )
            }
        }
    }
}