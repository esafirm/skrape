package nolambda.skrape.processor

import nolambda.skrape.nodes.Page

interface PageAdapter<out T> {
    fun adapt(page: Page): T
}