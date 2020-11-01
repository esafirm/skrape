package nolambda.skrape.processor.jsoup

import kotlinx.serialization.json.JsonPrimitive
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
        name to when (value.valueType) {
            Value.TYPE_BOOL -> JsonPrimitive(text.toBoolean())
            Value.TYPE_INT -> JsonPrimitive(text.toInt())
            else -> JsonPrimitive(text)
        }
    }
}