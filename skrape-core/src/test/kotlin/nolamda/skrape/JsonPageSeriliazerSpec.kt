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
            "info" to container {
                "coolness" to attr("alt")
            }
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
              "children": [
                {
                  "type": "value",
                  "name": "score"
                },
                {
                  "type": "container",
                  "name": "info",
                  "children": [
                    {
                      "type": "attr",
                      "name": "coolness",
                      "attrName": "alt"
                    }
                  ]
                }
              ]
            },
            {
              "type": "query",
              "cssSelector": "td a.storylink",
              "name": "items",
              "children": [
                {
                  "type": "value",
                  "name": "text"
                },
                {
                  "type": "attr",
                  "name": "link",
                  "attrName": "href"
                }
              ]
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
    }

    "it deserialize to page" {
        val result = serializer.deserialize(pageString)

        result.children.size shouldBe 2
        result.pageInfo.path shouldBe "https://news.ycombinator.com/"

        (result.children.first() as ParentElement).children.size shouldBe 2

        serializer.serialize(result) shouldBe pageString
    }
})