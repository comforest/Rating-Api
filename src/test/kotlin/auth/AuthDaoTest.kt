package auth

import Configure
import com.nomean.rating.api.auth.AuthDao
import com.nomean.rating.api.auth.dto.ThirdPartyTokenVo
import com.nomean.rating.api.auth.dto.UserInfoVo
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class AuthDaoTest : Configure() {
    @Autowired
    private lateinit var dao: AuthDao

    private val dataList = mutableListOf<Pair<ThirdPartyTokenVo, UserInfoVo>>()

    init {
        for (i in 1..3) {
            dataList.add(
                Pair(
                    ThirdPartyTokenVo("naver", "N-token${i}"),
                    UserInfoVo(i, "Name$i")
                )
            )
        }

        for (i in 4..6) {
            dataList.add(
                Pair(
                    ThirdPartyTokenVo("kakao", "K-token${i - 3}"),
                    UserInfoVo(i, "Name$i")
                )
            )
        }
    }


    override fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)

        for (data in dataList) {
            val auth = data.first
            val user = data.second

            setDB(
                "INSERT INTO users(user_id, nickname, resource, third_party_token) values(${user.id}, '${user.nickname}', '${auth.resource}', '${auth.token}');",
                "INSERT INTO auth(user_id, refresh_token, expire_date) values(${user.id}, 'R${auth.token}', DATEADD(hh, 1, NOW()));",
            )
        }
    }

    override fun afterEach(testCase: TestCase, result: TestResult) {
        super.afterEach(testCase, result)
        setDB("DELETE FROM auth", "DELETE FROM users", "ALTER TABLE users AUTO_INCREMENT = 1")
    }

    init {
        describe("Get User By Third Party") {
            context("Success") {
                for (data in dataList) {
                    val auth = data.first
                    val user = data.second

                    it("${user.id} ${user.nickname}") {
                        val result = dao.getUserInfoByThirdParty(auth.resource, auth.token)
                        result?.id shouldBe user.id
                        result?.nickname shouldBe user.nickname
                    }
                }
            }
            context("Fail - Wrong Data") {
                forAll(
                    row("Naver", "N-token1"),
                    row("Kakao", "K-token1"),
                    row("naver", "N-Token"),
                    row("kakao", "N-Token"),
                ) { resource, token ->
                    it("$resource $token") {
                        dao.getUserInfoByThirdParty(resource, token) shouldBe null
                    }
                }
            }
        }

        describe("Get User By Refresh Token") {
            context("Suceess") {
                for (data in dataList) {
                    val auth = data.first
                    val user = data.second

                    it("${user.id} ${user.nickname}") {
                        val result = dao.getUserInfoByRefreshToken("R${auth.token}")
                        result?.id shouldBe user.id
                        result?.nickname shouldBe user.nickname
                    }
                }
            }

            context("Fail - Wrong Token") {
                forAll(
                    row("R-token0"),
                    row("R-token"),
                    row("R-Token1"),
                ) { token ->
                    it("$token") {
                        dao.getUserInfoByRefreshToken(token) shouldBe null
                    }
                }
            }

            context("Fail - Token Expire") {
                setDB("UPDATE auth SET expire_date = NOW()")
                for (data in dataList) {
                    val token = "R${data.first.token}"
                    it(token) {
                        dao.getUserInfoByRefreshToken(token) shouldBe null
                    }
                }
            }
        }


        it("Create New User") {

            val resource = "Test-Resource"
            val token = "T-token1"

            dao.createNewUser(resource, token)

            val resultSet =
                selectDB("SELECT a.*, u.nickname FROM auth a RIGHT JOIN users u using(user_id) where user_id=7")
            resultSet.next() shouldBe true
            resultSet.getString("resource") shouldBe resource
            resultSet.getString("third_party_token") shouldBe token
            resultSet.getString("nickname") shouldBe null
            resultSet.getString("refresh_token") shouldNotBe null
            resultSet.next() shouldBe false
        }



        describe("Set RefreshToken") {
            forAll(row(3, "ADF", 1000 * 60 * 60 * 24)) { userId, token, expireDiff ->
                it("$userId $token $expireDiff") {

                    val expireDate = Date()
                    expireDate.time += expireDiff
                    dao.setRefreshToken(userId, token, expireDate)

                    val resultSet =
                        selectDB("SELECT user_id, refresh_token, expire_date FROM auth where user_id=$userId")
                    resultSet.next() shouldBe true
                    resultSet.getString("refresh_token") shouldBe token
                    resultSet.getString("expireDate") shouldBe expireDate
                    resultSet.next() shouldBe false
                }
            }
        }


    }
}