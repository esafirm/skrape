package nolambda.skrape.processor.jsoup

import com.github.salomonbrys.kotson.toJson
import nolambda.skrape.nodes.Value
import nolambda.skrape.processor.formatter.ValueFormatter
import org.jsoup.nodes.Element

class JsoupValueFormatter : ValueFormatter<Element, JsoupParserResult> {

    override fun isForType(value: Value): Boolean {
        return value.valueType.let {
            it == Value.TYPE_STRING || it == Value.TYPE_INT || it == Value.TYPE_BOOL
        }
    }

    private fun extractValue(query: String, element: Element): String {
        return if (query.isBlank()) {
            element.text()
        } else {
            element.select(query).text()
        }
    }

    override fun format(value: Value, element: Element): JsoupParserResult = with(value) {
        val text = extractValue(value.selector, element)
        name to when (value.type) {
            Value.TYPE_BOOL -> text.toBoolean().toJson()
            Value.TYPE_INT -> text.toInt().toJson()
            else -> text.toJson()
        }
    }
}