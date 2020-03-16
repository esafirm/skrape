package nolambda.skrape.result

import com.google.gson.JsonElement

open class SimpleSkrapeResult(private val jsonElement: JsonElement) : SkrapeResult {

    private val jsonString by lazy { jsonElement.toString() }

    override fun json(): String = jsonString

    override fun toString(): String = json()
}