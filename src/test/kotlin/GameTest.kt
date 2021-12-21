import com.nomean.rating.api.game.GameDao
import com.nomean.rating.api.game.GameVO
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.inject.Inject
import javax.sql.DataSource

@ExtendWith(RestDocumentationExtension::class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
class GameTest : DocConfigure() {

    @Autowired
    private lateinit var repository: GameDao
    private val answer = listOf(
        GameVO(1, "game1", 1, 2),
        GameVO(2, "game2", 2, 5),
        GameVO(3, "game3", 2, 4)
    )

    override fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)
        setDB(*Array(answer.size){ i->
            //language=SQL
            "INSERT INTO games(name, min_num, max_num) values('${answer[i].name}', ${answer[i].minNumber}, ${answer[i].maxNumber})"
        })
    }

    override fun afterEach(testCase: TestCase, result: TestResult) {
        super.afterTest(testCase, result)
        setDB("DELETE FROM games", "ALTER TABLE games AUTO_INCREMENT = 1")
    }

    init {
        it("getGameList()") {
            val list = repository.getGameList()

            list shouldContainExactly answer
        }


        it("addGame") {
            val game = GameVO(4, "testGame", 1, 4)
            repository.addGame(game)
            val list = repository.getGameList()
            val resultGame = list[3]

            resultGame shouldBe game
        }

        it("Doc Test") {
            mockMvc.perform(get("/v1/games").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andDo(document("index"))
        }
    }
}