import io.kotest.core.annotation.Ignored
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCaseOrder
import io.kotest.extensions.spring.SpringExtension
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.sql.Statement
import javax.inject.Inject
import javax.sql.DataSource


@ExtendWith(MockKExtension::class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@TestPropertySource("classpath:/config/database.test.properties")
@Ignored
open class Configure : DescribeSpec() {
    override fun testCaseOrder() = TestCaseOrder.Sequential
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        MockKAnnotations.init(this)
    }

    @Inject
    private lateinit var ds: DataSource

    protected fun setDB(@Language("SQL") vararg queries: String) {
        db {
            for (query in queries) {
                execute(query)
            }
        }
    }

    protected fun db(call: Statement.() -> Unit) {
        val connection = ds.connection
        val statement = connection.createStatement()

        call(statement)

        connection.close()
        statement.close()
    }
}