package nolambda.skrape.test

import nolambda.skrape.*
import nolambda.skrape.processor.jsoup.JsoupDocumentParser

fun main(args: Array<String>) {
    Page("https://news.ycombinator.com/") {
        "items" to query("td a.storylink") {
            "text" to text()
            "link" to attr("href")
        }
    }.run {
        Skrape(JsoupDocumentParser()).request(this)
    }
}