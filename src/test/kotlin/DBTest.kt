import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldNotBe
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionTemplate
import org.springframework.test.context.ContextConfiguration
import java.sql.Connection
import javax.inject.Inject
import javax.sql.DataSource


@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
class DBTest : AnnotationSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)


    @Inject
    private lateinit var ds: DataSource

    @Inject
    private lateinit var sqlSessionFactory: SqlSessionFactory

    @Inject
    private lateinit var sqlSessionTemplate: SqlSessionTemplate

    @Test
    fun testTemplate() {
        println(sqlSessionTemplate)
    }

    @Test
    fun testFactory() {
        println(sqlSessionFactory)
    }

    @Test
    @Throws(Exception::class)
    fun testSession() {
        val sqlSession = sqlSessionFactory.openSession()
        println(sqlSession)
    }

    @Test
    @Throws(Exception::class)
    fun testConnection() {
        val conn: Connection = ds.connection
        println(conn)
    }
}