package nolambda.kgdom

import nolambda.kgdom.processor.DocumentParser
import nolambda.kgdom.KgLogger as logger

class Kgdom<out T>(val parser: DocumentParser<T>) {

    fun request(page: Page) {
        logger.log("Requesting $page …")
        logger.log("Result ${parser.parse(page)}")
    }
}
