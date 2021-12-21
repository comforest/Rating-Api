import io.kotest.core.annotation.Ignored
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.inject.Inject
import javax.sql.DataSource


@ExtendWith(RestDocumentationExtension::class)
@WebAppConfiguration
@Ignored
@ContextConfiguration("file:src/main/webapp/WEB-INF/dispatcher-servlet.xml")
open class DocConfigure : Configure() {

    @Autowired
    private lateinit var context: WebApplicationContext

    @RegisterExtension
    val restDocumentation = ManualRestDocumentation()
    protected lateinit var mockMvc: MockMvc


    override fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(
                MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(Preprocessors.removeHeaders("Foo"))
                    .withResponseDefaults(Preprocessors.prettyPrint())
//                    .uris().apply {
//                        withScheme("http")
//                        withHost("test.com")
//                        withPort(8000)
//                    }
            )
            .build()

        restDocumentation.beforeTest(javaClass, testCase.name.testName)
    }

    override fun afterTest(testCase: TestCase, result: TestResult) {
        super.afterTest(testCase, result)
        restDocumentation.afterTest()
    }
}