package nolamda.skrape

import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import nolambda.skrape.result.QuerySkrapeResult

class QuerySkapeResultSpec : StringSpec({
    val result = QuerySkrapeResult(
        jsonArray(
            jsonObject(
                "title" to "How to kill a dragon",
                "rating" to 5
            ),
            jsonObject(
                "title" to "How to kill a meme",
                "rating" to 10
            )
        )
    )

    val toJsonObj = { string: String -> Gson().fromJson(string, JsonObject::class.java) }
    val toJsonArray = { string: String -> Gson().fromJson(string, JsonArray::class.java) }

    "it should have the right count" {
        toJsonObj(result.count).get("count").asInt shouldBe 2
    }

    "it should have the json result" {
        result.json().isNotBlank() shouldBe true
    }

    "it should get the right index" {
        val firstIndex = result.at(1)
        val obj = toJsonObj.invoke(firstIndex)
        obj.get("title").asString.contains("meme") shouldBe true
    }

    "it should get the right item" {
        val memeItem = result.find("title=meme")
        val obj = toJsonArray.invoke(memeItem)
        obj.first().get("title").asString.contains("meme") shouldBe true
    }
})