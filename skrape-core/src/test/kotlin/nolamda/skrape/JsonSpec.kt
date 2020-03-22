package nolamda.skrape

import com.google.gson.GsonBuilder
import io.kotlintest.specs.StringSpec
import nolambda.skrape.nodes.*
import nolambda.skrape.serialization.JsonPageSerializer

/**
 * This spec is a helper for creating json from Skrape
 */
class JsonSpec : StringSpec({

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

    val serializer = JsonPageSerializer(
        GsonBuilder()
            .setPrettyPrinting()
            .create()
    )

    "generate json" {
        println(serializer.serialize(page))
    }
})