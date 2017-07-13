package nolambda.skrape.processor.jsoup

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.toJson
import com.google.gson.JsonObject
import nolambda.skrape.SkrapeLogger
import nolambda.skrape.nodes.*
import nolambda.skrape.processor.DocumentParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.File

class JsoupDocumentParser : DocumentParser<String> {

    override fun parse(page: Page): String {
        val document = getDocument(page)
        return jsonObject().apply {
            processElement(page, document, this)
        }.toString()
    }

    fun getDocument(page: Page): Document {
        val (path, baseUrl, encoding) = page.pageInfo

        if (page.isLocalFile()) {
            val file = File(path)
            return Jsoup.parse(file, encoding, baseUrl)
        } else {
            return Jsoup.connect(baseUrl).get()
        }
    }

    fun processPage(page: Page, element: Element, json: JsonObject) = with(page) {
        body()
        children.map {
            processElement(it, element, json)
        }
    }

    fun processQuery(query: Query, element: Element, json: JsonObject) = with(query) {
        body()
        val jsonArray = jsonArray()
        element.select(cssSelector).map { child ->
            val childJson = jsonObject()
            children.map {
                processElement(it, child, childJson)
            }
            jsonArray.add(childJson)
        }
        json.add(name, jsonArray)
    }

    fun processText(text: Text, element: Element, json: JsonObject) = with(text) {
        json.add(name, element.text().toJson())
    }

    fun processAttr(attr: Attr, element: Element, json: JsonObject) = with(attr) {
        json.add(name, element.attr(attrName).toJson())
    }

    fun processElement(kgElement: KGElement, element: Element, json: JsonObject) {
        SkrapeLogger.log("$kgElement")

        when (kgElement) {
            is ParentElement -> when (kgElement) {
                is Page -> processPage(kgElement, element, json)
                is Query -> processQuery(kgElement, element, json)
            }
            is Text -> processText(kgElement, element, json)
            is Attr -> processAttr(kgElement, element, json)
        }
    }
}