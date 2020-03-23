package nolambda.skrape.nodes

import java.io.File

object ElementName {
    const val ELEMENT_PAGE = "page"
    const val ELEMENT_QUERY = "query"
    const val ELEMENT_CONTAINER = "container"
    const val ELEMENT_ATTR = "attr"
    const val ELEMENT_VALUE = "value"
}

interface Node {
    var name: String
    var type: String
}

sealed class SkrapeElemenet : Node

abstract class ParentElement : SkrapeElemenet() {
    abstract val body: ElementBody
    val children = arrayListOf<SkrapeElemenet>()
}

typealias ElementBody = ParentElement.() -> Unit

/* --------------------------------------------------- */
/* > Parent Elements */
/* --------------------------------------------------- */

class Page(
    val pageInfo: PageInfo,
    override var name: String = "",
    @Transient override val body: ElementBody
) : ParentElement() {

    constructor(path: String, baseUrl: String = "", body: ElementBody) : this(PageInfo(path, baseUrl), body = body)
    constructor(file: File, baseUrl: String = "", body: ElementBody) : this(file.path, baseUrl, body)

    override fun toString(): String = "Page(pageInfo=$pageInfo, name='$name', body=$body)"

    override var type: String = ElementName.ELEMENT_PAGE
}


class Query(
    val cssSelector: String,
    override var name: String = "",
    @Transient override val body: ElementBody
) : ParentElement() {
    override var type: String = ElementName.ELEMENT_QUERY
    override fun toString(): String = "Query(name='$name', cssSelector='$cssSelector')"
}

class Container(
    override var name: String = "",
    @Transient override val body: ElementBody
) : ParentElement() {
    override var type: String = ElementName.ELEMENT_CONTAINER
    override fun toString(): String = "Container(name='$name', body=$body)"
}

/* --------------------------------------------------- */
/* > Child Elements */
/* --------------------------------------------------- */

class Attr(
    override var name: String = "",
    val attrName: String
) : SkrapeElemenet() {
    override var type: String = ElementName.ELEMENT_ATTR
    override fun toString(): String = "Attr(name='$name', attrName='$attrName')"
}

class Value<T : Any>(
    override var name: String = "",
    @Transient val clazz: Class<T>,
    val query: String = ""
    ) : SkrapeElemenet() {
    override var type: String = ElementName.ELEMENT_VALUE
    override fun toString(): String {
        return "Value(name='$name', clazz=$clazz, query='$query')"
    }

}
