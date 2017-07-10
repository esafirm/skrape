package nolambda.kgdom.processor

import nolambda.kgdom.Page

interface DocumentParser<out T> {
    fun parse(page: Page): T
}