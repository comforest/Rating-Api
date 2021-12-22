package auth

import DocConfigure
import DocSnippet
import com.nomean.rating.api.auth.AuthService
import com.nomean.rating.api.auth.dto.UserInfoVo
import io.kotest.core.spec.Spec
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class AuthControllerTest : DocConfigure() {

//    @Autowired
//    @InjectMockKs(injectImmutable = true, overrideValues = true)
//    private lateinit var controller: AuthController

    private val service: AuthService = mockk(relaxed = true)

    var descriptionSnippet: DocSnippet =
        DocSnippet("Returns all workspaces for the authenticated profile.")

    private val userNaver = UserInfoVo(1, "naver user")
    private val userKakao = UserInfoVo(2, "kakao user")

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)

        every { service.login(any(), any()) } throws Exception()
        every { service.login("naver", TOKEN_NAVER) } returns userNaver
        every { service.login("kakao", TOKEN_KAKAO) } returns userKakao
    }


    init {
        describe("Login Test") {
            context("Naver") {
                it("Success") {
                    mockMvc
                        .perform(
                            post("/v1/auth/login")
                                .accept(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk)
                        .andDo(document("auth/naver", DocSnippet("T")))
                }
            }

            context("Kakao") {
                it("Success") {
                    mockMvc
                        .perform(
                            post("/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"resource\":\"naver\"}")
                                .accept(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().isOk)
                        .andDo(
                            document(
                                "auth/kakao",
                                DocSnippet("Login"),
                                requestFields(
                                    fieldWithPath("resource").type(JsonFieldType.STRING).description("서드파티 제공자 구분 +\n naver|kakao")
                                ),
                                responseFields(
                                    fieldWithPath("accessToken").type(JsonFieldType.STRING).description("아무튼 설명을 매우길게길게길게길게길게하면 어찌될까요 하하하하호호호후후후후 제발조미;ㄹㅇ ㅁ;니ㅓㅇ라ㅓ"),
                                    fieldWithPath("refreshToken").type(JsonFieldType.STRING).optional().description("The user's contact details"),
                                )
                            )
                        )
                }
            }
        }
    }

    companion object {
        private const val TOKEN_KAKAO = "TOKEN_KAKAO"
        private const val TOKEN_NAVER = "TOKEN_NAVER"
    }
}