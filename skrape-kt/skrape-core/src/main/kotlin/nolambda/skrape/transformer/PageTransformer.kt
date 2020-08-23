package nolambda.skrape.transformer

import nolambda.skrape.nodes.Page

interface PageTransformer<T> {
    fun transform(page: Page): Page
}