package nolambda.skrape.processor.jsoup

import com.github.salomonbrys.kotson.toJson
import nolambda.skrape.nodes.Value
import nolambda.skrape.processor.formatter.ValueFormatter
import org.jsoup.nodes.Element

class JsoupValueFormatter : ValueFormatter<Element, JsoupParserResult> {

    override fun isForType(value: Value<*>): Boolean {
        return value.clazz.let {
            it == Boolean::class.java || it == Int::class.java || it == String::class.java
        }
    }

    private fun extractValue(query: String, element: Element): String {
        return if (query.isBlank()) {
            element.text()
        } else {
            element.select(query).text()
        }
    }

    override fun format(value: Value<*>, element: Element): JsoupParserResult = with(value) {
        val text = extractValue(value.query, element)
        name to when (value.clazz) {
            Boolean::class.java -> text.toBoolean().toJson()
            Int::class.java -> text.toInt().toJson()
            else -> text.toJson()
        }
    }
}