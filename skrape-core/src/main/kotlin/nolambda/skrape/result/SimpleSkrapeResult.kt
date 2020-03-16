package nolambda.skrape.result

import com.google.gson.JsonElement

class SimpleSkrapeResult(private val jsonElement: JsonElement) : SkrapeResult {

    private val jsonString by lazy { jsonElement.toString() }

    override fun json(): String = jsonString

    override fun toString(): String = json()
}