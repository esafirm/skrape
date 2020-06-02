package nolamda.skrape

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.StringSpec
import nolambda.skrape.nodes.*
import nolambda.skrape.transformer.PlaceholderTransformer
import kotlin.to

class PlaceholderTransformerSpec : StringSpec({

    val transformer = PlaceholderTransformer(mapOf(
        "ngasal" to "tweet",
        "COBA" to "a"
    ))

    val page = Page("https://ngasal.com/{{ngasal}}") {
        query("td {{ngasal}}") {
            "place" to text()
            query("td {{COBA}}") {
                "another" to text()
                "place" to attr("href")
            }
        }
    }

    val resultPage = transformer.transform(page)

    "it should replace path placeholder" {
        val expectedPath = "https://ngasal.com/tweet"
        resultPage.pageInfo.path shouldBe expectedPath
    }

    "it should replace css selector placeholder" {
        val expectedSelector = "td tweet"

        val query = resultPage.children.first() as Query
        query.selector shouldBe expectedSelector
    }

    "it should throw if there's unfulfilled" {
        val failingTransformer = PlaceholderTransformer(emptyMap())
        shouldThrow<IllegalArgumentException> {
            failingTransformer.transform(page)
        }
    }
})