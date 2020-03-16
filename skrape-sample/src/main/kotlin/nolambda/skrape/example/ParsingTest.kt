package nolambda.skrape.example

import nolambda.skrape.Skrape
import nolambda.skrape.nodes.*
import nolambda.skrape.processor.jsoup.JsoupPageAdapter

fun main() {

    val mobileUa = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) " +
        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Mobile Safari/537.36"

    val parser = JsoupPageAdapter {
        this.userAgent(mobileUa)
    }

    val skrape = Skrape(parser)

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

    Page("https://jalantikus.com/news/") {
        "articles" to query("div.news-detail h2") {
            "title" to text()
        }
    }.run {
        skrape.request(this)
    }
}