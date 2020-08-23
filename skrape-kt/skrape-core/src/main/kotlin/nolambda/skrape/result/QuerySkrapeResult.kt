package nolambda.skrape.result

import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.keys
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

/**
 * This result support two kind of [JsonElement]
 * 1. JsonObject with one array as the child
 * {
 *  "items": [
 *      "a", "b", "c"
 *  ]
 *
 * 2. JsonArray
 */
class QuerySkrapeResult(
    private val jsonElement: JsonElement
) : SimpleSkrapeResult(jsonElement) {

    companion object {
        private const val KEY_FILTER = "filter"
        private const val KEY_ITEMS = "items"
        private const val KEY_COUNT = "count"

        private const val QUERY_SEPARATOR = "="
    }

    private val items: JsonArray by lazy {
        when (jsonElement) {
            is JsonArray -> jsonElement
            is JsonObject -> jsonElement.getAsJsonArray(KEY_ITEMS)
            else -> throw IllegalStateException("The json element must be one of JsonArray or JsonObject")
        }
    }

    val count by lazy {
        jsonObject(
            KEY_COUNT to items.size()
        ).toString()
    }

    fun at(index: Int) = items.get(index).toString()

    fun find(query: String): String {
        val (key, value) = query.split(QUERY_SEPARATOR).map { it.toLowerCase() }
        val filtered = items.filter {
            when (it) {
                is JsonPrimitive -> key == KEY_FILTER && it.asString.contains(value)
                is JsonObject -> {
                    val itemKey = it.keys().first()
                    val itemValue = it[itemKey].asString
                    itemKey.contains(key) && itemValue.contains(value)
                }
                else -> false
            }
        }
        return filtered.toString()
    }

}