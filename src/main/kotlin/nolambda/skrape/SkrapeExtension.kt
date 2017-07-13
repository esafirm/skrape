package nolambda.skrape

import nolambda.skrape.nodes.*

fun ParentElement.query(cssSelector: String, body: ElementBody): SkrapeElemenet =
        Query(cssSelector = cssSelector, body = body).apply {
            postCreate(this@query, this)
        }

fun ParentElement.text(): SkrapeElemenet =
        Text(node = this).apply {
            postCreate(this@text, this)
        }

fun ParentElement.attr(attrName: String): SkrapeElemenet =
        Attr(node = this, attrName = attrName).apply {
            postCreate(this@attr, this)
        }

internal fun postCreate(parent: ParentElement, child: SkrapeElemenet) {
    parent.children.add(child)
}

infix fun String.to(element: SkrapeElemenet): SkrapeElemenet = element.apply { name = this@to }
