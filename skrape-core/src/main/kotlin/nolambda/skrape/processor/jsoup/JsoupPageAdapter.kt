package nolambda.skrape.processor.jsoup

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.toJson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import nolambda.skrape.SkrapeLogger
import nolambda.skrape.nodes.*
import nolambda.skrape.processor.AbstractPageAdapter
import nolambda.skrape.processor.formatter.addFormatter
import nolambda.skrape.result.QuerySkrapeResult
import nolambda.skrape.result.SkrapeResult
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
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

    override fun adapt(page: Page): SkrapeResult {
        val document = getDocument(page)
        return QuerySkrapeResult(processPage(page, document))
    }

    private fun getDocument(page: Page): Document {
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

    private fun processPage(page: Page, element: Element): JsonElement = with(page) {
        evaluate()

        if (isUselessContainer()) {
            processChildren(page, element).map { it.second }.first()
        } else {
            jsonObject(processChildren(page, element))
        }
    }

    private fun processChildren(
        page: Page,
        element: Element
    ) = with(page) {
        children.map {
            processElement(it, element)
        }
    }

    private fun processQuery(query: Query, element: Element): Pair<String, JsonArray> = with(query) {
        body.invoke(query)

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

    private fun processContainer(container: Container, element: Element): Pair<String, JsonElement> = with(container) {
        body.invoke(container)

        val jsonObject = jsonObject()

        children.map {
            processElement(it, element)
        }.forEach { (jsonName, jsonElement) ->
            jsonObject.add(jsonName, jsonElement)
        }

        name to jsonObject
    }

    private fun processValue(value: Value<*>, element: Element): JsoupParserResult = with(value) {
        return formatterManager.format(value, element)
    }

    private fun processAttr(attr: Attr, element: Element): Pair<String, JsonPrimitive> = with(attr) {
        name to element.attr(attrName).toJson()
    }

    private fun processElement(skrapeElemenet: SkrapeElemenet, element: Element): Pair<String, JsonElement> {
        SkrapeLogger.log("$skrapeElemenet")

        return when (skrapeElemenet) {
            is Query -> processQuery(skrapeElemenet, element)
            is Value<*> -> processValue(skrapeElemenet, element)
            is Attr -> processAttr(skrapeElemenet, element)
            is Container -> processContainer(skrapeElemenet, element)
            else -> throw IllegalStateException("Skrape Element undefined")
        }
    }
}