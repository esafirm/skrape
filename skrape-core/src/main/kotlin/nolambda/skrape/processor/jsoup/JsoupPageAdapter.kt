package nolambda.skrape.processor.jsoup

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.toJson
import com.google.gson.JsonElement
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
        val jsonArray = jsonArray()
        element.select(cssSelector).map { jsoupElement ->
            val jsonObject = jsonObject()
            children.map {
                processElement(it, jsoupElement)
            }.forEach { (jsonName, jsonElement) ->
                jsonObject.add(jsonName, jsonElement)
            }
            jsonObject
        }.forEach {
            jsonArray.add(it)
        }

        name to jsonArray
    }

    override fun processContainer(container: Container, element: Element): JsoupParserResult = with(container) {
        val jsonObject = jsonObject()

        children.map {
            processElement(it, element)
        }.forEach { (jsonName, jsonElement) ->
            jsonObject.add(jsonName, jsonElement)
        }

        name to jsonObject
    }

    override fun processAttr(attr: Attr, element: Element): JsoupParserResult = with(attr) {
        name to element.attr(attrName).toJson()
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
            jsonObject(results)
        }
        return QuerySkrapeResult(json)
    }
}