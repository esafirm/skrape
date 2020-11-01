package nolambda.skrape.result

import kotlinx.serialization.json.JsonElement

open class SimpleSkrapeResult(private val jsonElement: JsonElement) : SkrapeResult {

    private val jsonString by lazy { jsonElement.toString() }

    override fun json(): String = jsonString

    override fun toString(): String = json()
}