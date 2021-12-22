import io.kotest.core.annotation.Ignored
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.cli.CliDocumentation
import org.springframework.restdocs.cli.CliDocumentation.curlRequest
import org.springframework.restdocs.http.HttpDocumentation.httpRequest
import org.springframework.restdocs.http.HttpDocumentation.httpResponse
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


@ExtendWith(RestDocumentationExtension::class)
@WebAppConfiguration
@Ignored
@ContextConfiguration("file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
open class DocConfigure : Configure() {

    @Autowired
    private lateinit var context: WebApplicationContext

    @RegisterExtension
    val restDocumentation = ManualRestDocumentation()

    protected val mockMvc: MockMvc by lazy {
        MockMvcBuilders.webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(documentConfigure)
            .build()
    }

    private val documentConfigure by lazy {
        MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation).apply {
            operationPreprocessors()
                .withRequestDefaults(org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders("Host"))
                .withResponseDefaults(org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint())
        }.apply {
            uris()
                .withScheme("http")
                .withHost("localhost.com")
                .withPort(8080)
        }.apply {
//            snippets()
//                .withDefaults(curlRequest(),
//                httpRequest(),
//                httpResponse(),
//                )
//                .withAdditionalDefaults()
//                .sni
        }
    }

    override fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)

        restDocumentation.beforeTest(javaClass, testCase.name.testName)
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        super.afterTest(testCase, result)
        restDocumentation.afterTest()
    }
}