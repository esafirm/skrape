package nolambda.skrape.example

import nolambda.skrape.Skrape
import nolambda.skrape.nodes.*
import nolambda.skrape.processor.chrome.ChromePageAdapter
import nolambda.skrape.processor.jsoup.JsoupPageAdapter
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

fun main() {
    val page = Page("https://news.ycombinator.com/") {
        "athing" to query("span.score") {
            "score" to text()
        }
        "items" to query("td a.storylink") {
            "text" to text()
            "link" to attr("href")
        }
    }

    val twitterPage = Page("https://twitter.com/lynxluna") {
        "bio" to text("main > div > div > div > div > div > div > div > div > div:nth-child(1) > div > div:nth-child(3) > div")
    }
    println(runChromeDriverSample(twitterPage))
}

private fun runJsoupSample(page: Page): String {
    println("Run Jsoup sample…")

    val mobileUa = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) " +
        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Mobile Safari/537.36"

    val parser = JsoupPageAdapter {
        this.userAgent(mobileUa)
    }

    val skrape = Skrape(parser)

    return skrape.request(page).json()
}

private fun runChromeDriverSample(page: Page): String {
    println("Run Chrome Driver sample…")

    val parser = ChromePageAdapter {
        ChromeDriver(ChromeOptions().apply {
            addArguments("--headless")
        })
    }
    val skrape = Skrape(parser)

    return skrape.request(page).json()
}