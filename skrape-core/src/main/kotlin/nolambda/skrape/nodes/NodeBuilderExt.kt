package nolambda.skrape.nodes

import kotlin.reflect.KClass

/* --------------------------------------------------- */
/* > Parent */
/* --------------------------------------------------- */

fun ParentElement.query(cssSelector: String, body: ElementBody): ParentElement =
    Query(cssSelector = cssSelector, body = body).apply {
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

private fun ParentElement.createValueElement(clazz: KClass<*>, query: String): SkrapeElemenet =
    Value(clazz = clazz.java, query = query).apply {
        postCreate(this@createValueElement, this)
    }

fun ParentElement.text(query: String = ""): SkrapeElemenet = createValueElement(String::class, query)
fun ParentElement.bool(query: String = ""): SkrapeElemenet = createValueElement(Boolean::class, query)
fun ParentElement.int(query: String = ""): SkrapeElemenet = createValueElement(Int::class, query)

internal fun postCreate(parent: ParentElement, child: SkrapeElemenet) {
    parent.children.add(child)
}

infix fun String.to(element: SkrapeElemenet): SkrapeElemenet = element.apply { name = this@to }
