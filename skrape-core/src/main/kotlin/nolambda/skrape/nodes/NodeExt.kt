package nolambda.skrape.nodes

/**
 * Evaluate all the children in this [Page]
 * It mainly use if the page want to be serialized or processed
 */
@Suppress("UNNECESSARY_SAFE_CALL")
fun <T : ParentElement> T.evaluate() = this.apply {
    if (children.isNotEmpty()) return@apply
    body?.invoke(this)
    evaluateChildren()
}

private fun ParentElement.evaluateChildren() {
    children.forEach { element ->
        if (element is ParentElement) {
            element.evaluate()
        }
    }
}