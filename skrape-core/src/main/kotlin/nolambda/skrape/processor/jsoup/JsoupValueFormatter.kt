package nolambda.skrape.processor.jsoup

import com.github.salomonbrys.kotson.toJson
import com.google.gson.JsonElement
import nolambda.skrape.nodes.Value
import nolambda.skrape.processor.formatter.ValueFormatter
import org.jsoup.nodes.Element

class JsoupValueFormatter : ValueFormatter<Element, Pair<String, JsonElement>> {

    override fun isForType(value: Value<*>): Boolean {
        return value.clazz.let {
            it == Boolean::class.java || it == Int::class.java
        }
    }

    override fun format(value: Value<*>, element: Element): Pair<String, JsonElement> = with(value) {
        val text = element.text()
        name to when (value.clazz) {
            Boolean::class.java -> text.toBoolean().toJson()
            Int::class.java -> text.toInt().toJson()
            else -> text.toJson()
        }
    }
}