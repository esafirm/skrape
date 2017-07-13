package nolambda.skrape.processor

import nolambda.skrape.nodes.Page

interface DocumentParser<out T> {
    fun parse(page: Page): T
}