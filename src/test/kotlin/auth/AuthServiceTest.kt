package auth

import Configure
import com.nomean.rating.api.auth.AuthService
import com.nomean.rating.api.auth.ThirdPartAuth
import com.nomean.rating.api.auth.dto.ThirdPartyTokenVo
import com.nomean.rating.api.auth.dto.UserInfoVo
import com.nomean.rating.api.auth.internal.KakaoAuth
import com.nomean.rating.api.auth.internal.NaverAuth
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.Spec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import org.springframework.beans.factory.annotation.Autowired


class AuthServiceTest : Configure() {

    private val auth: Map<String, ThirdPartAuth> = mockk()
    private val naverAuth: NaverAuth = mockk(relaxed = true)
    private val kakaoAuth: KakaoAuth = mockk(relaxed = true)

    @Autowired
    @InjectMockKs(injectImmutable = true, overrideValues = true)
    private lateinit var service: AuthService

    private val user = UserInfoVo(nickname = "HI")

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        every { naverAuth.getUserInfo(any()) } throws Exception()
        every { naverAuth.getUserInfo("naver-Success") } returns user
        every { naverAuth.validateLogin("naver-Success") } returns true

        every { kakaoAuth.getUserInfo(any()) } throws Exception()
        every { kakaoAuth.getUserInfo("kakao-Success") } returns user
        every { kakaoAuth.validateLogin("kakao-Success") } returns true

        every { auth["naverAuth"] } returns naverAuth
        every { auth["kakaoAuth"] } returns kakaoAuth
    }

    init {
        describe("Login Test") {
            forAll(
                row("naver"),
                row("kakao")
            ) { resource ->
                context(resource) {
                    it("로그인 성공") {
                        val user = service.login(ThirdPartyTokenVo(resource, "$resource-Success"))
                        user shouldBe this@AuthServiceTest.user
                    }

                    it("로그인 실패 - Token Invalid") {
                        shouldThrow<Exception> {
                            service.login(ThirdPartyTokenVo(resource, "Success"))
                        }
                    }
                }
            }

            context("Wrong Resource") {
                it("로그인 실패") {
                    shouldThrow<Exception> {
                        service.login(ThirdPartyTokenVo("resource", "Success"))
                    }
                }
            }
        }
    }
}