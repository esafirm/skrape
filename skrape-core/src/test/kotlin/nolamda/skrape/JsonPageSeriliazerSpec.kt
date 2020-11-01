package nolamda.skrape

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import kotlinx.serialization.json.Json
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
              "selector": "span.score",
              "name": "athing",
              "children": [
                {
                  "type": "value",
                  "name": "score",
                  "valueType": "string",
                  "selector": ""
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
              "selector": "td a.storylink",
              "name": "items",
              "children": [
                {
                  "type": "value",
                  "name": "text",
                  "valueType": "string",
                  "selector": ""
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

    val serializer = JsonPageSerializer(Json {
        prettyPrint = true
        encodeDefaults = true
    })

    "it serialize to string" {
        val result = serializer.serialize(page)
        result.contains(Regex("ycombinator")) shouldBe true
    }

    "it deserialize to page" {
        val result = serializer.deserialize(pageString)

        result.children.size shouldBe 2
        result.pageInfo.path shouldBe "https://news.ycombinator.com/"

        (result.children.first() as ParentElement).children.size shouldBe 2

        val value = (result.children.first() as ParentElement).children.first() as Value
        value.valueType shouldBe Value.TYPE_STRING

        val secondResult = serializer.deserialize(serializer.serialize(result))

        secondResult.children.size shouldBe 2
        secondResult.pageInfo.path shouldBe "https://news.ycombinator.com/"
    }
})