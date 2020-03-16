package nolambda.skrape

import nolambda.skrape.nodes.Page
import nolambda.skrape.processor.PageAdapter
import nolambda.skrape.SkrapeLogger as logger

class Skrape<out T>(private val parser: PageAdapter<T>) {

    fun request(page: Page): T {
        logger.log("Requesting $this")
        return parser.adapt(page).also { logger.log("Result $it") }
    }
}
