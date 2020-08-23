package nolambda.skrape.transformer

import nolambda.skrape.nodes.*

class PlaceholderTransformer(
    private val args: Map<String, String>
) : PageTransformer<Page> {

    companion object {
        private val PLACEHOLDER_PATTERN = Regex(".*\\{\\{(.*)}}.*")
    }

    override fun transform(page: Page): Page {
        page.evaluate()

        val pageInfo = page.pageInfo
        return page.copy(pageInfo = pageInfo.copy(path = pageInfo.path.replacePlaceholder())).apply {
            setNewChildren(transformChildren(page.children))
        }
    }

    private fun transformChildren(children: List<SkrapeElemenet>): List<SkrapeElemenet> {
        return children.map {
            when (it) {
                is Query -> it.copy(selector = it.selector.replacePlaceholder()).also { query ->
                    query.setNewChildren(transformChildren(it.children))
                }
                else -> it
            }
        }
    }

    private fun ParentElement.setNewChildren(newChildren: List<SkrapeElemenet>) {
        children.clear()
        children.addAll(newChildren)
    }

    private fun String.replacePlaceholder(): String {
        val results = PLACEHOLDER_PATTERN.findAll(this)
        if (results.count() == 0) return this

        val finalResult = results.fold(this) { acc, result ->
            val capturedKey = result.groupValues[1]
            args[capturedKey]?.let { acc.replace("{{${capturedKey}}}", it) } ?: acc
        }

        // Check if there's un-fulfilled placeholder
        if (PLACEHOLDER_PATTERN.matches(finalResult)) {
            throw IllegalArgumentException("Unfulfilled placeholder on: $this")
        }

        return finalResult
    }
}