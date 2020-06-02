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

data class Page(
    val pageInfo: PageInfo,
    override var name: String = "",
    @Transient override val body: ElementBody
) : ParentElement() {

    constructor(path: String, baseUrl: String = "", body: ElementBody) : this(PageInfo(path, baseUrl), body = body)
    constructor(file: File, baseUrl: String = "", body: ElementBody) : this(file.path, baseUrl, body)

    override var type: String = ElementName.ELEMENT_PAGE
}


data class Query(
    val selector: String,
    override var name: String = "",
    @Transient override val body: ElementBody
) : ParentElement() {
    override var type: String = ElementName.ELEMENT_QUERY
}

data class Container(
    override var name: String = "",
    @Transient override val body: ElementBody
) : ParentElement() {
    override var type: String = ElementName.ELEMENT_CONTAINER
}

/* --------------------------------------------------- */
/* > Child Elements */
/* --------------------------------------------------- */

data class Attr(
    override var name: String = "",
    val attrName: String
) : SkrapeElemenet() {
    override var type: String = ElementName.ELEMENT_ATTR
}

typealias ValueType = String

data class Value(
    override var name: String = "",
    val valueType: ValueType = TYPE_STRING,
    val selector: String = ""
) : SkrapeElemenet() {

    companion object {
        const val TYPE_STRING: ValueType = "string"
        const val TYPE_BOOL: ValueType = "bool"
        const val TYPE_INT: ValueType = "int"
    }

    override var type: String = ElementName.ELEMENT_VALUE
}
