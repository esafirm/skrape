package nolambda.skrape.serialization

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import nolambda.skrape.nodes.*

class JsonPageSerializer(
    private val json: Json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }
) : PageSerializer<String> {

    override fun serialize(page: Page): String {
        return json.encodeToString(page.evaluate())
    }

    override fun deserialize(target: String): Page {
        val jsonObject = json.decodeFromString<JsonObject>(target)
        val pageInfo = json.decodeFromJsonElement<PageInfo>(jsonObject[PageSerializer.KEY_PAGE_INFO]!!)

        return Page(pageInfo = pageInfo, name = jsonObject.getName()) {
            children.addAll(createChildrenFromJsonArray(jsonObject.getChildArray()))
        }.evaluate()
    }

    private fun mapTypeToClass(type: String) = when (type) {
        ElementName.ELEMENT_QUERY -> Query::class.java
        ElementName.ELEMENT_CONTAINER -> Container::class.java
        ElementName.ELEMENT_ATTR -> Attr::class.java
        ElementName.ELEMENT_VALUE -> Value::class.java
        else -> throw IllegalArgumentException("Not a valid page JSON!")
    }

    private fun createElementFromJson(element: JsonElement): SkrapeElemenet {
        val type = element.jsonObject[PageSerializer.KEY_TYPE]!!.jsonPrimitive.content
        val clazz = mapTypeToClass(type)
        return if (ParentElement::class.java.isAssignableFrom(clazz)) {
            createParentElement(clazz, element)
        } else {
            json.decodeFromJsonElement(element)
        }
    }

    private fun <T : SkrapeElemenet> createParentElement(clazz: Class<T>, content: JsonElement): SkrapeElemenet {
        return when (clazz) {
            Query::class.java -> Query(
                content.jsonObject[PageSerializer.KEY_SELECTOR]!!.jsonPrimitive.content,
                content.getName(),
                content.createBody()
            )
            Container::class.java -> Container(
                content.getName(),
                content.createBody()
            )
            else -> throw IllegalArgumentException("Not a valid parent class!")
        }
    }

    private fun createChildrenFromJsonArray(children: JsonArray): List<SkrapeElemenet> {
        return children.map { child: JsonElement ->
            createElementFromJson(child)
        }
    }

    private fun JsonElement.getChildArray(): JsonArray {
        return when (this) {
            is JsonObject -> get(PageSerializer.KEY_PAGE_CHILDREN) as JsonArray
            else -> throw IllegalStateException("Element is not an object: $this")
        }
    }

    private fun JsonElement.getName() = when (this) {
        is JsonObject -> get(PageSerializer.KEY_NAME)!!.jsonPrimitive.content
        else -> throw IllegalStateException("Element is not an object: $this")
    }

    private fun JsonElement.createBody(): ElementBody {
        val json = this
        return { children.addAll(createChildrenFromJsonArray(json.getChildArray())) }
    }
}