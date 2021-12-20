//import com.nomean.rating.api.game.GameDao
//import com.nomean.rating.api.game.GameVO
//import io.kotest.core.test.TestCase
//import io.kotest.core.test.TestResult
//import io.kotest.matchers.collections.shouldContainExactly
//import io.kotest.matchers.shouldBe
//import io.mockk.impl.recording.WasNotCalled.method
//import io.restassured.RestAssured
//import io.restassured.builder.RequestSpecBuilder
//import io.restassured.specification.RequestSpecification
//import org.junit.jupiter.api.extension.ExtendWith
//import org.junit.jupiter.api.extension.RegisterExtension
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.http.MediaType
//import org.springframework.restdocs.ManualRestDocumentation
//import org.springframework.restdocs.RestDocumentationExtension
////import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
////import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
//import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
//import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration
//import org.springframework.test.context.web.WebAppConfiguration
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//import java.sql.SQLException
//import javax.inject.Inject
//import javax.sql.DataSource
//
//
//@ExtendWith(RestDocumentationExtension::class)
//@WebAppConfiguration
//class GameTest : Configure() {
//    @Inject
//    private lateinit var ds: DataSource
//
//    @Autowired
//    private lateinit var repository: GameDao
//
//
//    private lateinit var spec: RequestSpecification
//
////    @Autowired
////    private lateinit var context: WebApplicationContext
////    private lateinit var mockMvc: MockMvc
//
//    @RegisterExtension
//    val restDocumentation = ManualRestDocumentation()
//
//
//    override fun beforeEach(testCase: TestCase) {
//        super.beforeEach(testCase)
//        setTestDB()
//
//
//        this.spec = RequestSpecBuilder()
//            .addFilter(documentationConfiguration(restDocumentation))
//            .setBaseUri("http://localhost")
//            .build()
////        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
////            .apply<DefaultMockMvcBuilder>(documentationConfiguration(this.restDocumentation))
////            .build()
//        restDocumentation.beforeTest(javaClass, testCase.name.testName)
//    }
//
//    override fun afterTest(testCase: TestCase, result: TestResult) {
//        super.afterTest(testCase, result)
//        restDocumentation.afterTest()
//    }
//
//    private fun setTestDB() {
//        val connection = ds.connection
//        val statement = connection.createStatement()
//
//        try {
//            statement.execute("DELETE FROM games")
//            statement.execute("ALTER TABLE games AUTO_INCREMENT = 1")
//            statement.execute("INSERT INTO games(name, min_num, max_num) values('game1', 1, 2)")
//            statement.execute("INSERT INTO games(name, min_num, max_num) values('game2', 2, 5)")
//            statement.execute("INSERT INTO games(name, min_num, max_num) values('game3', 2, 4)")
//        } catch (e: SQLException) {
//            e.printStackTrace()
//        } finally {
//            statement.close()
//            connection.close()
//        }
//    }
//
//    init {
//        it("getGameList()") {
//            val list = repository.getGameList()
//            val answer = listOf(
//                GameVO(1, "game1", 1, 2),
//                GameVO(2, "game2", 2, 5),
//                GameVO(3, "game3", 2, 4)
//            )
//            list shouldContainExactly answer
//        }
//
//
//        it("addGame") {
//            val game = GameVO(4, "testGame", 1, 4)
//            repository.addGame(game)
//            val list = repository.getGameList()
//            val resultGame = list[3]
//
//            resultGame shouldBe game
//        }
//
//        it("Doc Test") {
//            RestAssured.given(spec)
//                .accept("application/json")
//                .filter(document("index"))
//                .`when`().get("/v1/games")
//                .then().assertThat().statusCode(200)
//
////            mockMvc.perform(get("/v1/games").accept(MediaType.APPLICATION_JSON))
////                .andExpect(status().isOk)
////                .andDo(document("index"))
//        }
//    }
//}