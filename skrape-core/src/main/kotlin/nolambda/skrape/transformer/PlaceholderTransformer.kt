package nolambda.skrape.transformer

import nolambda.skrape.nodes.Page
import nolambda.skrape.nodes.evaluate

class PlaceholderTransformer(
    private val args: Map<String, String>
) : PageTransformer<Page> {

    companion object {
        private val PLACEHOLDER_PATTERN = Regex(".*\\{\\{(.*)}}.*")
    }

    override fun transform(page: Page): Page {
        page.evaluate()

        val pageInfo = page.pageInfo
        val page = page.copy(pageInfo = pageInfo.copy(path = pageInfo.path.replacePlaceholder(args)))

        return page
    }

    private fun String.replacePlaceholder(args: Map<String, String>): String {
        val results = PLACEHOLDER_PATTERN.findAll(this)
        if (results.count() == 0) return this

        return results.fold(this) { acc, result ->
            val capturedKey = result.groupValues[1]
            args[capturedKey]?.let { acc.replace("{{${capturedKey}}}", it) } ?: acc
        }
    }
}