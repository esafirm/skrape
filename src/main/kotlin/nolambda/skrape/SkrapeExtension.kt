package nolambda.skrape

fun ParentElement.query(cssSelector: String, body: ElementBody): KGElement =
        Query(cssSelector = cssSelector, body = body).apply {
            postCreate(this@query, this)
        }

fun ParentElement.text(): KGElement =
        Text(node = this).apply {
            postCreate(this@text, this)
        }

fun ParentElement.attr(attrName: String): KGElement =
        Attr(node = this, attrName = attrName).apply {
            postCreate(this@attr, this)
        }

internal fun postCreate(parent: ParentElement, child: KGElement) {
    parent.children.add(child)
}

infix fun String.to(element: KGElement): KGElement = element.apply { name = this@to }
