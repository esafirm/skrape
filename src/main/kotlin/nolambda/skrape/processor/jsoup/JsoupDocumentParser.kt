package nolambda.skrape.processor.jsoup

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.toJson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
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
        return processPage(page, document).toString()
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

    fun processPage(page: Page, element: Element): JsonObject = with(page) {
        body.invoke(page)

        jsonObject().apply {
            children.map {
                processElement(it, element)
            }.map { (name, jsonElement) ->
                add(name, jsonElement)
            }
        }
    }

    fun processQuery(query: Query, element: Element): Pair<String, JsonArray> = with(query) {
        body.invoke(query)

        val jsonArray = jsonArray()
        element.select(cssSelector).map { jsoupElement ->
            val jsonObject = jsonObject()
            children.map {
                processElement(it, jsoupElement)
            }.map { (jsonName, jsonElement) ->
                jsonObject.add(jsonName, jsonElement)
            }
            jsonObject
        }.forEach {
            jsonArray.add(it)
        }

        name to jsonArray
    }

    fun processText(text: Text, element: Element): Pair<String, JsonPrimitive> = with(text) {
        name to element.text().toJson()
    }

    fun processAttr(attr: Attr, element: Element): Pair<String, JsonPrimitive> = with(attr) {
        name to element.attr(attrName).toJson()
    }

    fun processElement(skrapeEle: SkrapeElemenet, element: Element): Pair<String, JsonElement> {
        SkrapeLogger.log("$skrapeEle")

        return when (skrapeEle) {
            is Query -> processQuery(skrapeEle, element)
            is Text -> processText(skrapeEle, element)
            is Attr -> processAttr(skrapeEle, element)
            else -> throw IllegalStateException("Skrape Element undefined")
        }
    }
}