//package auth
//
//import Configure
//import com.nomean.rating.api.auth.AuthDao
//import com.nomean.rating.api.auth.dto.UserAuthVO
//import com.nomean.rating.api.auth.dto.UserInfoVo
//import io.kotest.core.test.TestCase
//import io.kotest.data.forAll
//import io.kotest.data.row
//import io.kotest.matchers.shouldBe
//import io.kotest.matchers.shouldNotBe
//import org.springframework.beans.factory.annotation.Autowired
//import java.util.*
//
//class AuthDaoTest : Configure() {
//    @Autowired
//    private lateinit var dao: AuthDao
//
//    private val dataList = mutableListOf<Pair<UserAuthVO, UserInfoVo>>()
//
//    init {
//        for (i in 1..3) {
//            dataList.add(
//                Pair(
//                    UserAuthVO(i, "naver", "N-token${i}", "R-token$i"),
//                    UserInfoVo(i, "Name$i")
//                )
//            )
//            val id = i + 3
//            dataList.add(
//                Pair(
//                    UserAuthVO(id, "kakao", "K-token${i}", "R-token$id"),
//                    UserInfoVo(id, "Name$id")
//                )
//            )
//        }
//    }
//
//
//    override fun beforeEach(testCase: TestCase) {
//        super.beforeEach(testCase)
//        for (data in dataList) {
//            val auth = data.first
//            val user = data.second
//            statement.execute("INSERT INTO auth(id, resource, third_party_token, refresh_token, expire_date) values(${auth.id}, ${auth.refreshToken}, ${auth.thirdPartToken}, ${auth.refreshToken}, SELECT DATE_ADD(NOW(), INTERVAL 1 HOUR);)")
//            statement.execute("INSERT INTO users(id, nickname) values(${user.id}, ${user.nickname})")
//        }
//    }
//
//    init {
//        describe("Get User By Third Party") {
//            context("Success") {
//                for (data in dataList) {
//                    val auth = data.first
//                    val user = data.second
//
//                    it("${auth.id} ${user.nickname}") {
//                        val result = dao.getUserInfoByThirdParty(auth.resource!!, auth.thirdPartToken!!)
//                        result?.id shouldBe user.id
//                        result?.nickname shouldBe user.nickname
//                    }
//                }
//            }
//            context("Fail - Wrong Data") {
//                forAll(
//                    row("Naver", "N-token1"),
//                    row("Kakao", "K-token1"),
//                    row("naver", "N-Token"),
//                    row("kakao", "N-Token"),
//                ) { resource, token ->
//                    it("$resource $token") {
//                        dao.getUserInfoByThirdParty(resource, token) shouldBe null
//                    }
//                }
//            }
//        }
//
//        describe("Get User By Refresh Token") {
//            context("Suceess") {
//                for (data in dataList) {
//                    val auth = data.first
//                    val user = data.second
//
//                    it("${auth.id} ${user.nickname}") {
//                        val result = dao.getUserInfoByRefreshToken(auth.refreshToken!!)
//                        result?.id shouldBe user.id
//                        result?.nickname shouldBe user.nickname
//                    }
//                }
//            }
//
//            context("Fail - Wrong Token") {
//                forAll(
//                    row("R-token0"),
//                    row("R-token"),
//                    row("R-Token1"),
//                ) { token ->
//                    it("$token") {
//                        dao.getUserInfoByRefreshToken(token) shouldBe null
//                    }
//                }
//            }
//
//            context("Fail - Token Expire") {
//                statement.execute("UPDATE auth SET expire_date = NOW()")
//                for (data in dataList) {
//                    val token = data.first.refreshToken!!
//                    it(token) {
//                        dao.getUserInfoByRefreshToken(token) shouldBe null
//                    }
//                }
//            }
//        }
//
//
//        it("Create New User") {
//            val user = UserAuthVO(7, "Test", "T-Token1")
//            dao.createNewUser(user)
//
//            val resultSet =
//                statement.executeQuery("SELECT a.*, u.nickname FROM auth a RIGHT JOIN users u using(user_id) where user_id=${user.id}")
//            resultSet.next() shouldBe true
//            resultSet.getString("resource") shouldBe user.resource
//            resultSet.getString("third_party_token") shouldBe user.thirdPartToken
//            resultSet.getString("nickname") shouldBe null
//            resultSet.getString("refresh_token") shouldNotBe null
//            resultSet.next() shouldBe false
//        }
//
//
//
//        describe("Set RefreshToken") {
//            forAll(row(3, "ADF", 1000 * 60 * 60 * 24)) { userId, token, expireDiff ->
//                it("$userId $token $expireDiff") {
//
//                    val expireDate = Date()
//                    expireDate.time += expireDiff
//                    dao.setRefreshToken(userId, token, expireDate)
//
//                    val resultSet =
//                        statement.executeQuery("SELECT user_id, refresh_token, expire_date FROM auth where user_id=$userId")
//                    resultSet.next() shouldBe true
//                    resultSet.getString("refresh_token") shouldBe token
//                    resultSet.getString("expireDate") shouldBe expireDate
//                    resultSet.next() shouldBe false
//                }
//            }
//        }
//
//
//    }
//}