package nolambda.skrape.processor

import nolambda.skrape.Page

interface DocumentParser<out T> {
    fun parse(page: Page): T
}