package nolamda.skrape

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import nolambda.skrape.result.QuerySkrapeResult

class QuerySkapeResultSpec : StringSpec({
    val result = QuerySkrapeResult(
        JsonArray(listOf(
            JsonObject(mapOf(
                "title" to JsonPrimitive("How to kill a dragon"),
                "rating" to JsonPrimitive(5)
            )),
            JsonObject(mapOf(
                "title" to JsonPrimitive("How to kill a meme"),
                "rating" to JsonPrimitive(10)
            ))
        ))
    )

    val toJsonObj = { string: String -> Json.decodeFromString<JsonObject>(string) }
    val toJsonArray = { string: String -> Json.decodeFromString<JsonArray>(string) }

    "it should have the right count" {
        toJsonObj(result.count).get("count")?.jsonPrimitive?.int shouldBe 2
    }

    "it should have the json result" {
        result.json().isNotBlank() shouldBe true
    }

    "it should get the right index" {
        val firstIndex = result.at(1)
        val obj = toJsonObj.invoke(firstIndex)
        obj.get("title").toString().contains("meme") shouldBe true
    }

    "it should get the right item" {
        val memeItem = result.find("title=meme")
        val obj = toJsonArray.invoke(memeItem)
        obj.first().jsonObject["title"].toString().contains("meme") shouldBe true
    }
})