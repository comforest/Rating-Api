package com.nomean.rating.api.auth

import Configure
import com.nomean.rating.api.auth.AuthDao
import com.nomean.rating.api.auth.dto.ThirdPartyTokenVo
import com.nomean.rating.api.auth.dto.UserInfoVo
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.ints.shouldNotBeLessThanOrEqual
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

        setDB("INSERT INTO auth(user_id, refresh_token, expire_date) values(${dataList[0].second.id}, 'R2${dataList[0].first.token}', DATEADD(hh, 1, NOW()));")
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
                        val result = dao.getUserInfoByThirdParty(auth)
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
                        dao.getUserInfoByThirdParty(ThirdPartyTokenVo(resource, token)) shouldBe null
                    }
                }
            }
        }

        describe("Get User By Refresh Token") {
            context("Succeess") {
                for (data in dataList) {
                    val auth = data.first
                    val user = data.second

                    it("${user.id} ${user.nickname}") {
                        val result = dao.getUserInfoByRefreshToken("R${auth.token}")
                        result?.id shouldBe user.id
                        result?.nickname shouldBe user.nickname
                    }
                }

                it("Success using second token") {
                    val auth = dataList[0].first
                    val user = dataList[0].second

                    val result = dao.getUserInfoByRefreshToken("R2${auth.token}")
                    result?.id shouldBe user.id
                    result?.nickname shouldBe user.nickname
                }
            }

            context("Fail - Wrong Token") {
                forAll(
                    row("RK-token0"),
                    row("RN-token"),
                    row("R-Token1"),
                ) { token ->
                    it(token) {
                        dao.getUserInfoByRefreshToken(token) shouldBe null
                    }
                }
            }

            context("Fail - Token Expire") {
                for (data in dataList) {
                    val token = "R${data.first.token}"
                    it(token) {
                        setDB("UPDATE auth SET expire_date = NOW()")
                        dao.getUserInfoByRefreshToken(token) shouldBe null
                    }
                }
            }
        }

        it("Create New User") {

            val resource = "TResource"
            val token = "T-token1"

            dao.createUser(ThirdPartyTokenVo(resource, token))


            db {
                val resultSet =
                    executeQuery("SELECT resource, third_party_token, nickname FROM users where user_id=${dataList.size + 1}")

                resultSet.next() shouldBe true
                resultSet.getString("resource") shouldBe resource
                resultSet.getString("third_party_token") shouldBe token
                resultSet.getString("nickname") shouldBe null
                resultSet.next() shouldBe false
            }
        }

        describe("Delete User") {
            // TODO : foreign key 처리
            for (id in 1..dataList.size) {
                it("$id") {
                    dao.deleteUser(id)

                    db {
                        val resultSet =
                            executeQuery("SELECT resource, third_party_token, nickname FROM users where user_id=$id")
                        resultSet.next() shouldBe false
                    }
                }
            }
        }

        describe("Add Refresh Token") {
            context("Second Refresh Token") {
                for (i in 2 until dataList.size) {
                    it("$i") {
                        val auth = dataList[i].first
                        val user = dataList[i].second
                        val expireDate = Date()
                        expireDate.time += 1000 * 60 * 60

                        dao.addRefreshToken(user.id, "R2${auth.token}", expireDate)

                        db {
                            val resultSet =
                                executeQuery("SELECT user_id, refresh_token, expire_date FROM auth where user_id=${user.id}")
                            resultSet.next() shouldBe true
                            resultSet.getString("refresh_token") shouldBe "R${auth.token}"
                            resultSet.getDate("expire_date") shouldNotBe null
                            resultSet.next() shouldBe true
                            resultSet.getString("refresh_token") shouldBe "R2${auth.token}"
                            resultSet.getTimestamp("expire_date").time shouldBe expireDate.time
                            resultSet.next() shouldBe false
                        }
                    }
                }
            }

            context("Third Refresh Token") {
                it("1") {
                    val auth = dataList[0].first
                    val user = dataList[0].second
                    val expireDate = Date()
                    expireDate.time += 1000 * 60 * 60

                    dao.addRefreshToken(user.id, "R3${auth.token}", expireDate)

                    db {
                        val resultSet =
                            executeQuery("SELECT user_id, refresh_token, expire_date FROM auth where user_id=${user.id}")
                        resultSet.next() shouldBe true
                        resultSet.getString("refresh_token") shouldBe "R${auth.token}"
                        resultSet.getDate("expire_date") shouldNotBe null
                        resultSet.next() shouldBe true
                        resultSet.getString("refresh_token") shouldBe "R2${auth.token}"
                        resultSet.getDate("expire_date") shouldNotBe null
                        resultSet.next() shouldBe true
                        resultSet.getString("refresh_token") shouldBe "R3${auth.token}"
                        resultSet.getTimestamp("expire_date").time shouldBe expireDate.time
                        resultSet.next() shouldBe false
                    }
                }
            }
        }


        describe("Delete Refresh Token") {
            context("Success") {
                for (data in dataList) {
                    val auth = data.first
                    val user = data.second

                    it("R${auth.token}") {
                        dao.deleteRefreshToken(user.id, "R${auth.token}")

                        db {
                            val resultSet = executeQuery("SELECT * FROM auth where refresh_token = 'R${auth.token}'")
                            resultSet.next() shouldBe false
                        }
                    }
                }
            }

            context("Not Match userId - token") {
                for (data in dataList) {
                    val auth = data.first

                    it("R${auth.token}") {
                        dao.deleteRefreshToken(0, "R${auth.token}")
                        db {
                            val resultSet = executeQuery("SELECT * FROM auth where refresh_token = 'R${auth.token}'")
                            resultSet.next() shouldBe true
                        }
                    }
                }
            }
        }


        it("Delete Refresh Token Expired") {
            setDB("UPDATE auth SET expire_date = NOW() where user_id <= 3")
            dao.deleteExpiredRefreshToken()

            db {
                val resultSet = executeQuery("SELECT user_id FROM auth")
                var hasNext = resultSet.next()
                hasNext shouldBe true
                while (hasNext) {
                    resultSet.getInt("user_id") shouldNotBeLessThanOrEqual 3
                    hasNext = resultSet.next()
                }
            }
        }

    }
}