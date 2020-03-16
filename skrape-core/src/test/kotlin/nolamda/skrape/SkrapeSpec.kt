package nolamda.skrape

import com.google.gson.Gson
import com.google.gson.JsonArray
import io.kotlintest.matchers.beGreaterThan
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.specs.StringSpec
import nolambda.skrape.Skrape
import nolambda.skrape.nodes.*
import nolambda.skrape.processor.jsoup.JsoupPageAdapter
import java.io.File

typealias StringSkrape = Skrape<String>

class SkrapeSpec : StringSpec() {
    init {
        val skrape = Skrape(JsoupPageAdapter())
        val gson = Gson()

        "it parsing from local file" {
            val result = requestWithFile(skrape, ::createFirstPage)
            val response = gson.fromJson(result, HackerNewsResponse::class.java)

            result shouldNotBe null
            response shouldNotBe null
            response.stories[0] shouldNotBe null
        }

        "it parsing from url" {
            requestWithUrl(skrape) shouldNotBe null
        }

        "it support un-named query" {
            val result = requestWithFile(skrape, ::createSecondPagee)
            val array = gson.fromJson(result, JsonArray::class.java)

            array.size() shouldBe beGreaterThan(1)
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

fun requestWithFile(skrape: StringSkrape, pageCreator: (File) -> Page): String {
    val classLoader = ClassLoader.getSystemClassLoader()
    val file = File(classLoader.getResource("index.html").file)

    return pageCreator(file).run {
        skrape.request(this)
    }
}


fun requestWithUrl(skrape: StringSkrape) {
    Page("https://news.ycombinator.com/") {
        "athing" to query("span.score") {
            "score" to text()
        }
        "items" to query("td a.storylink") {
            "text" to text()
            "link" to attr("href")
        }
    }.run {
        skrape.request(this)
    }
}