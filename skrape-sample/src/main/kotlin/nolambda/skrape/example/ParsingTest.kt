package nolambda.skrape.example

import nolambda.skrape.Skrape
import nolambda.skrape.nodes.*
import nolambda.skrape.processor.jsoup.JsoupDocumentParser

fun main(args: Array<String>) {
    val skrape = Skrape(JsoupDocumentParser())

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