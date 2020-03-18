package nolambda.skrape.serialization

import nolambda.skrape.nodes.Page

interface PageSerializer<TARGET> {

    companion object {
        const val KEY_PAGE_CHILDREN = "children"
        const val KEY_PAGE_INFO = "pageInfo"
        const val KEY_NAME = "name"
        const val KEY_TYPE= "type"
        const val KEY_CSS_SELECTOR = "cssSelector"
    }

    fun serialize(page: Page): TARGET
    fun deserialize(target: TARGET): Page
}