package nolambda.skrape.processor.jsoup

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.toJson
import com.google.gson.*
import nolambda.skrape.SkrapeLogger
import nolambda.skrape.nodes.*
import nolambda.skrape.processor.AbstractDocumentParser
import nolambda.skrape.processor.formatter.addFormatter
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.helper.HttpConnection
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.File

typealias JsoupParserResult = Pair<String, JsonElement>

class JsoupDocumentParser(val connection: (Connection.() -> Unit)? = null) : AbstractDocumentParser<Element, JsoupParserResult, String>() {
    
    init {
        addFormatter(JsoupValueFormatter())
    }

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
            val connect = HttpConnection.connect(path)
            connection?.invoke(connect)
            return connect.get()
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
    
    fun processContainer(container: Container, element: Element): Pair<String, JsonElement> = with(container) {
        body.invoke(container)
        
        val jsonObject = jsonObject()
        
        children.map {
            processElement(it, element)
        }.forEach { (jsonName, jsonElement) ->
            jsonObject.add(jsonName, jsonElement)
        }
        
        name to jsonObject
    }
    
    fun processValue(value: Value<*>, element: Element): JsoupParserResult = with(value) {
        if (formatterManager.isForType(value)) {
            return formatterManager.format(value, element)
        }
        name to element.text().toJson()
    }
    
    fun processAttr(attr: Attr, element: Element): Pair<String, JsonPrimitive> = with(attr) {
        name to element.attr(attrName).toJson()
    }
    
    fun processElement(skrapeElemenet: SkrapeElemenet, element: Element): Pair<String, JsonElement> {
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