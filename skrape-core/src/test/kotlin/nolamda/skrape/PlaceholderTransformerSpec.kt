package nolamda.skrape

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import nolambda.skrape.nodes.Page
import nolambda.skrape.nodes.query
import nolambda.skrape.nodes.text
import nolambda.skrape.transformer.PlaceholderTransformer

class PlaceholderTransformerSpec : StringSpec({

    val transformer = PlaceholderTransformer(mapOf(
        "ngasal" to "tweet"
    ))

    val page = Page("https://ngasal.com/{{ngasal}}") {
        query("td {{ngasal}}") {
            "place" to text()
        }
    }

    val resultPage = transformer.transform(page)

    "it should replace all the placeholder" {
        val expectedPath = "https://ngasal.com/tweet"
        resultPage.pageInfo.path shouldBe expectedPath
    }
})