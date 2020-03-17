package nolambda.skrape.nodes

import java.io.File

object ElementName {
    const val ELEMENT_PAGE = "page"
    const val ELEMENT_QUERY = "query"
    const val ELEMENT_CONTAINER = "container"
    const val ELEMENT_ATTR = "attr"
    const val ELEMENT_VALUE = "valuee"
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

    @Transient
    private val URL_REGEX = Regex("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")

    fun isLocalFile(): Boolean = !pageInfo.path.matches(URL_REGEX)

    fun isUselessContainer(): Boolean {
        assert(children.isNotEmpty()) { "Page must have children!" }
        if (children.size > 1) {
            return false
        }
        val firstChild = children.first()
        return firstChild is Query && firstChild.name.isBlank()
    }

    /**
     * Evaluate all the children in this [Page]
     * It mainly use if the page want to be serialized or processed
     */
    fun evaluate() = this.apply {
        if (children.isEmpty()) {
            body.invoke(this)
        }
    }

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
    override fun toString(): String = "Wrapper(name='$name', body=$body)"
}

/* --------------------------------------------------- */
/* > Child Elements */
/* --------------------------------------------------- */

class Attr(
    override var name: String = "",
    val node: Node,
    val attrName: String
) : SkrapeElemenet() {
    override var type: String = ElementName.ELEMENT_ATTR
    override fun toString(): String = "Attr(name='$name', nodes=$node, attrName='$attrName')"
}

class Value<T : Any>(
    override var name: String = "",
    val node: Node,
    val clazz: Class<T>
) : SkrapeElemenet() {
    override var type: String = ElementName.ELEMENT_VALUE
    override fun toString(): String = "Text(name='$name', nodes=$node)"
}
