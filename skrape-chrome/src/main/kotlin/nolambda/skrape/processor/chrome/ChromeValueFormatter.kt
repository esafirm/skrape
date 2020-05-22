package nolambda.skrape.processor.chrome

import com.github.salomonbrys.kotson.toJson
import nolambda.skrape.nodes.Value
import nolambda.skrape.processor.formatter.ValueFormatter

class ChromeValueFormatter : ValueFormatter<ChromeElement, ChromeParserResult> {

    override fun isForType(value: Value<*>): Boolean {
        return value.clazz.let {
            it == Boolean::class.java || it == Int::class.java || it == String::class.java
        }
    }

    private fun extractValue(query: String, element: ChromeElement): String {
        return if (query.isBlank()) {
            element.text()
        } else {
            element.findEl(query).first().text
        }
    }

    override fun format(value: Value<*>, element: ChromeElement): ChromeParserResult = with(value) {
        val text = extractValue(value.query, element)
        name to when (value.clazz) {
            Boolean::class.java -> text.toBoolean().toJson()
            Int::class.java -> text.toInt().toJson()
            else -> text.toJson()
        }
    }
}