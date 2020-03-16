package nolambda.skrape

import nolambda.skrape.nodes.Page
import nolambda.skrape.processor.DocumentParser
import nolambda.skrape.SkrapeLogger as logger

class Skrape<out T>(private val parser: DocumentParser<T>) {

    fun request(page: Page): T {
        logger.log("Requesting $this")
        return parser.parse(page).also { logger.log("Result $it") }
    }
}
