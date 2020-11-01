package nolambda.skrape.result

import kotlinx.serialization.json.*

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
            is JsonObject -> jsonElement[KEY_ITEMS] as JsonArray
            else -> throw IllegalStateException("The json element must be one of JsonArray or JsonObject")
        }
    }

    val count by lazy {
        val map = mapOf(
            KEY_COUNT to JsonPrimitive(items.size)
        )
        JsonObject(map).toString()
    }

    fun at(index: Int) = items[index].toString()

    fun find(query: String): String {
        val (key, value) = query.split(QUERY_SEPARATOR).map { it.toLowerCase() }
        val filtered = items.filter {
            when (it) {
                is JsonPrimitive -> key == KEY_FILTER && it.contentOrNull?.contains(value) == true
                is JsonObject -> {
                    val itemKey = it.keys.first()
                    val itemValue = it[itemKey]

                    if (itemValue is JsonPrimitive) {
                        itemKey.contains(key) && itemValue.contentOrNull?.contains(value) == true
                    } else false
                }
                else -> false
            }
        }
        return filtered.toString()
    }

}