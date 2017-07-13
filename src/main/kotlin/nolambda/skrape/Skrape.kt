package nolambda.skrape

import nolambda.skrape.nodes.Page
import nolambda.skrape.processor.DocumentParser
import nolambda.skrape.SkrapeLogger as logger

class Skrape<out T>(val parser: DocumentParser<T>) {

    fun request(page: Page) {
        logger.log("Requesting $page …")
        logger.log("Result ${parser.parse(page)}")
    }
}
