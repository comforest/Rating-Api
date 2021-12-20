import io.kotest.core.annotation.Ignored
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCaseOrder
import io.kotest.extensions.spring.SpringExtension
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement
import javax.inject.Inject
import javax.sql.DataSource


@ExtendWith(MockKExtension::class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
@TestPropertySource("classpath:/config/database.test.properties")
open class Configure : DescribeSpec() {
    override fun testCaseOrder() = TestCaseOrder.Sequential
    override fun extensions(): List<Extension> = listOf(SpringExtension, )

    @Inject
    private lateinit var ds: DataSource
    protected lateinit var connection : Connection
    protected lateinit var statement : Statement

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        MockKAnnotations.init(this)
        connection = ds.connection
        statement = connection.createStatement()
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        connection.close()
        statement.close()
    }
}