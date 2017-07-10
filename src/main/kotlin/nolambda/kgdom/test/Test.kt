package nolambda.kgdom.test

import nolambda.kgdom.*
import nolambda.kgdom.processor.jsoup.JsoupDocumentParser

fun main(args: Array<String>) {
    Page("https://news.ycombinator.com/") {
        "items" to query("td a.storylink") {
            "text" to text()
            "link" to attr("href")
        }
    }.run {
        Kgdom(JsoupDocumentParser()).request(this)
    }
}