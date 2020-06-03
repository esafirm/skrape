package nolambda.skrape.processor.chrome

import com.github.salomonbrys.kotson.toJson
import nolambda.skrape.nodes.Value
import nolambda.skrape.processor.formatter.ValueFormatter

class ChromeValueFormatter(
    private val waiter: () -> ChromeWaiter
) : ValueFormatter<ChromeElement, ChromeParserResult> {

    override fun isForType(value: Value): Boolean {
        return value.valueType.let {
            it == Value.TYPE_STRING || it == Value.TYPE_INT || it == Value.TYPE_BOOL
        }
    }

    private fun extractValue(query: String, element: ChromeElement): String {
        return if (query.isBlank()) {
            element.text()
        } else {
            element.findElWait(waiter(), query).first().text
        }
    }

    override fun format(value: Value, element: ChromeElement): ChromeParserResult = with(value) {
        val text = extractValue(value.selector, element)
        name to when (value.valueType) {
            Value.TYPE_BOOL -> text.toBoolean().toJson()
            Value.TYPE_INT -> text.toInt().toJson()
            else -> text.toJson()
        }
    }
}