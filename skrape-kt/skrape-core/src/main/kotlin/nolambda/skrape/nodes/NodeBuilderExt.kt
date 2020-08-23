package nolambda.skrape.nodes

/* --------------------------------------------------- */
/* > Parent */
/* --------------------------------------------------- */

fun ParentElement.query(cssSelector: String, body: ElementBody): ParentElement =
    Query(selector = cssSelector, body = body).apply {
        postCreate(this@query, this)
    }

fun ParentElement.container(body: ElementBody): ParentElement =
    Container(body = body).apply {
        postCreate(this@container, this)
    }

/* --------------------------------------------------- */
/* > Child */
/* --------------------------------------------------- */

fun ParentElement.attr(attrName: String): SkrapeElemenet =
    Attr(attrName = attrName).apply {
        postCreate(this@attr, this)
    }

private fun ParentElement.createValueElement(type: ValueType, query: String): SkrapeElemenet =
    Value(valueType = type, selector = query).apply {
        postCreate(this@createValueElement, this)
    }

fun ParentElement.text(query: String = ""): SkrapeElemenet = createValueElement(Value.TYPE_STRING, query)
fun ParentElement.bool(query: String = ""): SkrapeElemenet = createValueElement(Value.TYPE_BOOL, query)
fun ParentElement.int(query: String = ""): SkrapeElemenet = createValueElement(Value.TYPE_INT, query)

internal fun postCreate(parent: ParentElement, child: SkrapeElemenet) {
    parent.children.add(child)
}

infix fun String.to(element: SkrapeElemenet): SkrapeElemenet = element.apply { name = this@to }
