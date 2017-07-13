package nolambda.skrape.test

import nolambda.skrape.*
import nolambda.skrape.nodes.Page
import nolambda.skrape.processor.jsoup.JsoupDocumentParser
import java.io.File

typealias StringSkrape = Skrape<String>

fun main(args: Array<String>) {
    Skrape(JsoupDocumentParser()).run {
        requestWithLocalFile(this)
    }
}

fun requestWithLocalFile(skrape: StringSkrape) {
    val classLoader = ClassLoader.getSystemClassLoader()
    val file = File(classLoader.getResource("index.html").file)

    Page(file) {
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