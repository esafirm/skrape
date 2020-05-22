package nolambda.skrape.processor.chrome

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.toJson
import com.google.gson.JsonElement
import nolambda.skrape.nodes.Attr
import nolambda.skrape.nodes.Container
import nolambda.skrape.nodes.Page
import nolambda.skrape.nodes.Query
import nolambda.skrape.processor.AbstractPageAdapter
import nolambda.skrape.processor.formatter.addFormatter
import nolambda.skrape.result.QuerySkrapeResult
import nolambda.skrape.result.SkrapeResult
import org.openqa.selenium.chrome.ChromeDriver

typealias ChromeParserResult = Pair<String, JsonElement>

class ChromePageAdapter(
    options: ChromeDriver.() -> Unit
) : AbstractPageAdapter<ChromeElement, ChromeParserResult, SkrapeResult>() {

    private val driver = ChromeDriver().apply(options)

    init {
        addFormatter(ChromeValueFormatter())
    }

    override fun requestPage(page: Page): ChromeElement {
        driver.get(page.pageInfo.path)
        return ChromeElement.Driver(driver)
    }

    override fun onHandleResult(page: Page, results: List<ChromeParserResult>): SkrapeResult {
        return QuerySkrapeResult(jsonObject(results))
    }

    override fun processQuery(query: Query, element: ChromeElement): ChromeParserResult = with(query) {
        val children = element.findEl(cssSelector).map { webEl ->
            jsonObject(children.map {
                processElement(it, ChromeElement.Component(webEl))
            })
        }
        name to jsonArray(children)
    }

    override fun processContainer(container: Container, element: ChromeElement): ChromeParserResult = with(container) {
        val children = children.map {
            processElement(it, element)
        }
        name to jsonObject(children)
    }

    override fun processAttr(attr: Attr, element: ChromeElement): ChromeParserResult = with(attr) {
        name to element.attr(attrName).toJson()
    }

    override fun onEnd() {
        driver.close()
    }
}