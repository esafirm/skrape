package nolamda.skrape

import com.google.gson.GsonBuilder
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import nolambda.skrape.nodes.*
import nolambda.skrape.serialization.JsonPageSerializer

class JsonPageSeriliazerSpec : StringSpec({

    val page = Page("https://news.ycombinator.com/") {
        "athing" to query("span.score") {
            "score" to text()
        }
        "items" to query("td a.storylink") {
            "text" to text()
            "link" to attr("href")
        }
    }

    val pageString = """
        {
          "type": "page",
          "pageInfo": {
            "path": "https://news.ycombinator.com/",
            "baseUrl": "",
            "encoding": "UTF-8"
          },
          "name": "",
          "children": [
            {
              "type": "query",
              "cssSelector": "span.score",
              "name": "athing",
              "children": []
            },
            {
              "type": "query",
              "cssSelector": "td a.storylink",
              "name": "items",
              "children": []
            }
          ]
        }
    """.trimIndent()

    val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    val serializer = JsonPageSerializer(gson)

    "it serialize to string" {
        val result = serializer.serialize(page)
        result shouldBe pageString

        print(result)
    }

    "it deserialize to page" {
        val result = serializer.deserialize(pageString)
        serializer.serialize(result) shouldBe pageString

        print(result)
    }
})