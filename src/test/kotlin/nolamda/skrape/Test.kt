package nolamda.skrape

import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.specs.StringSpec
import nolambda.skrape.*
import nolambda.skrape.nodes.Page
import nolambda.skrape.processor.jsoup.JsoupDocumentParser
import java.io.File

typealias StringSkrape = Skrape<String>

class SkrapeTest : StringSpec() {
    init {
        val skrape = Skrape(JsoupDocumentParser())
        "Parsing local file" {
            requestWithFile(skrape) shouldNotBe null
        }
        "Parsing from url" {
            requestWithUrl(skrape) shouldNotBe null
        }
    }
}

fun requestWithFile(skrape: StringSkrape): String {
    val classLoader = ClassLoader.getSystemClassLoader()
    val file = File(classLoader.getResource("index.html").file)

    return Page(file) {
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


fun requestWithUrl(skrape: StringSkrape) {
    Page("https://news.ycombinator.com/") {
        "items" to query("td a.storylink") {
            "text" to text()
            "link" to attr("href")
        }
    }.run {
        skrape.request(this)
    }
}