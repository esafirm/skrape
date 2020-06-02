package nolambda.skrape.serialization

import com.github.salomonbrys.kotson.get
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import nolambda.skrape.nodes.*

class JsonPageSerializer(private val gson: Gson = Gson()) : PageSerializer<String> {

    override fun serialize(page: Page): String {
        return gson.toJson(page.evaluate())
    }

    override fun deserialize(target: String): Page {
        val jsonObject = gson.fromJson(target, JsonObject::class.java)
        val pageInfo = gson.fromJson(jsonObject[PageSerializer.KEY_PAGE_INFO], PageInfo::class.java)

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

    private fun createElementFromJson(json: JsonElement): SkrapeElemenet {
        val clazz = mapTypeToClass(json[PageSerializer.KEY_TYPE].asString)
        return if (ParentElement::class.java.isAssignableFrom(clazz)) {
            createParentElement(clazz, json)
        } else {
            json.fromJson(clazz)
        }
    }

    private fun <T : SkrapeElemenet> createParentElement(clazz: Class<T>, content: JsonElement): SkrapeElemenet {
        return when (clazz) {
            Query::class.java -> Query(
                content[PageSerializer.KEY_SELECTOR].asString,
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
        return children.map { child ->
            createElementFromJson(child)
        }
    }

    private fun JsonElement.getChildArray() =
        get(PageSerializer.KEY_PAGE_CHILDREN).asJsonArray

    private fun JsonElement.getName() =
        get(PageSerializer.KEY_NAME).asString

    private fun JsonElement.createBody(): ElementBody {
        val json = this
        return { children.addAll(createChildrenFromJsonArray(json.getChildArray())) }
    }

    private fun <T : SkrapeElemenet> JsonElement.fromJson(clazz: Class<T>): T {
        return gson.fromJson(this, clazz)
    }


}