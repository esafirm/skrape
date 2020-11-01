package nolambda.skrape

import io.kotlintest.specs.StringSpec
import kotlinx.serialization.json.Json
import nolambda.skrape.nodes.*
import nolambda.skrape.processor.jsoup.JsoupPageAdapter
import nolambda.skrape.serialization.JsonPageSerializer
import nolambda.skrape.utils.Queries

/**
 * This spec is a helper for creating json from Skrape
 */
class JsonSpec : StringSpec({

    val page = Page("https://news.ycombinator.com/") {
        "items" to query("td a.storylink") {
            "text" to text()
            "link" to attr("href")
        }
    }

    val secondPage = Page("https://kawalcovid19.id/") {
        "items" to query("div.css-1ll0e4o") {
            "title" to text(Queries.indexOfChild("span", 2))
            "count" to text(Queries.indexOfChild("span", 1))
        }
    }

    val thirdPage = Page("https://twitter.com") {
        "title" to text("td.a")
    }

    val serializer = JsonPageSerializer(
        Json {
            prettyPrint = true
            encodeDefaults = true
        }
    )

    "generate json" {
        println(serializer.serialize(page))
    }

    "generate result json" {
        val skrape = Skrape(JsoupPageAdapter {
            proxy(null)
            userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
            referrer("google.com")
        }, false)
        println(skrape.request(secondPage).json())
    }

    "generate thrid json" {
        println(serializer.serialize(thirdPage))
    }
})