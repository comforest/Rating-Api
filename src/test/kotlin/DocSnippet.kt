import org.springframework.restdocs.operation.Operation
import org.springframework.restdocs.snippet.TemplatedSnippet

class DocSnippet(title: String, description: String) :
    TemplatedSnippet("main", mapOf(Pair("title", title), Pair("description", description))) {

    constructor(title: String) : this(title, "")


    override fun createModel(operation: Operation): MutableMap<String, Any> {
        return mutableMapOf(
            Pair("request_field", operation.request.content.isNotEmpty()),
            Pair("response_field", operation.response.content.isNotEmpty()),
            Pair("method", operation.request.method),
            Pair("path", operation.request.uri.path)
        )
    }


}
