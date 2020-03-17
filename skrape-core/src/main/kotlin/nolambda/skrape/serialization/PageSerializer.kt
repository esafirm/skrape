package nolambda.skrape.serialization

import nolambda.skrape.nodes.Page

interface PageSerializer<TARGET> {

    companion object {
        const val KEY_PAGE_CHILDREN = "children"
        const val KEY_PAGE_INFO = "pageInfo"
        const val KEY_NAME = "name"
    }

    fun serialize(page: Page): TARGET
    fun deserialize(target: TARGET): Page
}