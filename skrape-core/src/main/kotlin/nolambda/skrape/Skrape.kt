package nolambda.skrape

import nolambda.skrape.nodes.Page
import nolambda.skrape.processor.PageAdapter
import nolambda.skrape.SkrapeLogger as logger

class Skrape<out T>(
    private val parser: PageAdapter<T>,
    private val enableLog: Boolean = false
) {

    fun request(page: Page): T {
        log { "Requesting $this" }
        return parser.adapt(page).also {
            log { "Result $it" }
        }
    }

    private inline fun log(log: () -> String) {
        if (enableLog) {
            logger.log(log())
        }
    }
}
