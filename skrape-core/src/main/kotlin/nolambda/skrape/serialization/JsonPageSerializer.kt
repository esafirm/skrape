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
        val pageInfo = gson.fromJson(jsonObject.get(PageSerializer.KEY_PAGE_INFO), PageInfo::class.java)

        return Page(pageInfo = pageInfo, name = jsonObject.get(PageSerializer.KEY_NAME).asString) {
            children.addAll(createChildrenFromJsonArray(jsonObject.getChildArray()))
        }
    }

    private fun mapTypeToClass(type: String) = when (type) {
        ElementName.ELEMENT_QUERY -> Query::class.java
        ElementName.ELEMENT_CONTAINER -> Container::class.java
        ElementName.ELEMENT_ATTR -> Attr::class.java
        ElementName.ELEMENT_VALUE -> Value::class.java
        else -> throw IllegalArgumentException("Not a valid page JSON!")
    }

    private fun createChildrenFromJson(child: JsonElement): SkrapeElemenet {
        val clazz = mapTypeToClass(child["type"].asString)
        if (clazz is ParentElement) {
            child.fromJson(clazz).apply {
                this as ParentElement
                this.children.addAll(createChildrenFromJsonArray(child.getChildArray()))
            }
        }
        return child.fromJson(clazz)
    }

    private fun createChildrenFromJsonArray(children: JsonArray): List<SkrapeElemenet> {
        return children.map { child ->
            createChildrenFromJson(child)
        }
    }

    private fun JsonElement.getChildArray() =
        gson.fromJson(get(PageSerializer.KEY_PAGE_CHILDREN), JsonArray::class.java)

    private fun <T : SkrapeElemenet> JsonElement.fromJson(clazz: Class<T>) =
        gson.fromJson(this, clazz)
}