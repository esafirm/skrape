package nolambda.skrape.nodes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
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
}

@Serializable
sealed class SkrapeElemenet : Node

@Serializable
sealed class ParentElement : SkrapeElemenet() {
    abstract val body: ElementBody
    val children = arrayListOf<SkrapeElemenet>()
}

typealias ElementBody = ParentElement.() -> Unit

/* --------------------------------------------------- */
/* > Parent Elements */
/* --------------------------------------------------- */

@Serializable
@SerialName(ElementName.ELEMENT_PAGE)
data class Page(
    val pageInfo: PageInfo,
    override var name: String = "",
    @Transient override val body: ElementBody = {}
) : ParentElement() {

    constructor(path: String, baseUrl: String = "", body: ElementBody) : this(PageInfo(path, baseUrl), body = body)
    constructor(file: File, baseUrl: String = "", body: ElementBody) : this(file.path, baseUrl, body)
}

@Serializable
@SerialName(ElementName.ELEMENT_QUERY)
data class Query(
    val selector: String,
    override var name: String = "",
    @Transient override val body: ElementBody = {}
) : ParentElement()

@Serializable
@SerialName(ElementName.ELEMENT_CONTAINER)
data class Container(
    override var name: String = "",
    @Transient override val body: ElementBody = {}
) : ParentElement()

/* --------------------------------------------------- */
/* > Child Elements */
/* --------------------------------------------------- */

/**
 * Attr fetch the [attrName] on the element
 */
@Serializable
@SerialName(ElementName.ELEMENT_ATTR)
data class Attr(
    override var name: String = "",
    val attrName: String
) : SkrapeElemenet()

typealias ValueType = String

/**
 * Value should process [query] inside the parent element if exist
 * if not, it will fetch text from the parent element instead
 * after that it will convert the data to expected type
 */
@Serializable
@SerialName(ElementName.ELEMENT_VALUE)
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
}
