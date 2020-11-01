package nolambda.skrape.processor.jsoup

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import nolambda.skrape.nodes.*
import nolambda.skrape.processor.AbstractPageAdapter
import nolambda.skrape.processor.formatter.addFormatter
import nolambda.skrape.result.QuerySkrapeResult
import nolambda.skrape.result.SkrapeResult
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File

typealias JsoupParserResult = Pair<String, JsonElement>
typealias JsoupConfig = Connection.() -> Unit

class JsoupPageAdapter(
    private val config: JsoupConfig = {}
) : AbstractPageAdapter<Element, JsoupParserResult, SkrapeResult>() {

    init {
        addFormatter(JsoupValueFormatter())
    }

    override fun processQuery(query: Query, element: Element): JsoupParserResult = with(query) {
        val children = element.select(selector).map { jsoupElement ->
            JsonObject(children.map {
                processElement(it, jsoupElement)
            }.toMap())
        }
        name to JsonArray(children)
    }

    override fun processContainer(container: Container, element: Element): JsoupParserResult = with(container) {
        val children = children.map {
            processElement(it, element)
        }
        name to JsonObject(children.toMap())
    }

    override fun processAttr(attr: Attr, element: Element): JsoupParserResult = with(attr) {
        name to JsonPrimitive(element.attr(attrName))
    }

    override fun requestPage(page: Page): Element {
        val (path, baseUrl, encoding) = page.pageInfo

        return if (page.isLocalFile()) {
            val file = File(path)
            Jsoup.parse(file, encoding, baseUrl)
        } else {
            Jsoup.connect(path)
                .apply(config)
                .get()
        }
    }

    override fun onHandleResult(page: Page, results: List<JsoupParserResult>): SkrapeResult {
        val json: JsonElement = if (page.isUselessContainer()) {
            results.map { it.second }.first()
        } else {
            JsonObject(results.toMap())
        }
        return QuerySkrapeResult(json)
    }
}