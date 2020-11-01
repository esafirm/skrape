package nolambda.skrape

import io.kotlintest.matchers.beGreaterThan
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.specs.StringSpec
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import nolambda.skrape.nodes.*
import nolambda.skrape.processor.jsoup.JsoupPageAdapter
import nolambda.skrape.result.SkrapeResult
import java.io.File

typealias SimpleSkrape = Skrape<SkrapeResult>

class SkrapeJsoupSpec : StringSpec() {
    init {
        val skrape = Skrape(JsoupPageAdapter(), enableLog = true)

        "it parsing from local file" {
            val result = requestWithFile(skrape, ::createFirstPage)
            val json = Json { ignoreUnknownKeys = true }
            val response = json.decodeFromString<HackerNewsResponse>(result)

            result shouldNotBe null
            response shouldNotBe null
            response.stories[0] shouldNotBe null
        }

        "it parsing from url" {
            requestWithUrl(skrape) shouldNotBe null
        }

        "it support un-named query" {
            val result = requestWithFile(skrape, ::createSecondPagee)
            val array = Json.decodeFromString<JsonArray>(result)

            array.size shouldBe beGreaterThan(1)
        }
    }
}

fun createFirstPage(file: File): Page {
    return Page(file) {
        "items" to query("td a.storylink") {
            "text" to text()
            "detail" to container {
                "link" to attr("href")
            }
        }
    }
}

fun createSecondPagee(file: File): Page {
    return Page(file) {
        query("td a.storylink") {
            "text" to text()
        }
    }
}

fun requestWithFile(skrape: SimpleSkrape, pageCreator: (File) -> Page): String {
    val classLoader = ClassLoader.getSystemClassLoader()
    val file = File(classLoader.getResource("index.html").file)

    return pageCreator(file).run {
        skrape.request(this).json()
    }
}


fun requestWithUrl(skrape: SimpleSkrape): String {
    return Page("https://news.ycombinator.com/") {
        "athing" to query("span.score") {
            "score" to text()
        }
        "items" to query("td a.storylink") {
            "text" to text()
            "link" to attr("href")
        }
    }.run {
        skrape.request(this)
    }.json()
}