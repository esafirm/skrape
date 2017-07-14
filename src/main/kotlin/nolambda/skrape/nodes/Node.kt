package nolambda.skrape.nodes

import java.io.File

interface Node {
    var name: String
}

sealed class SkrapeElemenet : Node

abstract class ParentElement : SkrapeElemenet() {
    val children = arrayListOf<SkrapeElemenet>()
}

typealias ElementBody = ParentElement.() -> Unit

/* --------------------------------------------------- */
/* > Parent Elements */
/* --------------------------------------------------- */

class Page(val pageInfo: PageInfo, override var name: String = "", val body: ElementBody) : ParentElement() {

    constructor(path: String, baseUrl: String = "", body: ElementBody) : this(PageInfo(path, baseUrl), body = body)
    constructor(file: File, baseUrl: String = "", body: ElementBody) : this(file.path, baseUrl, body)

    override fun toString(): String = "Page(pageInfo=$pageInfo, name='$name', body=$body)"

    val URL_REGEX = Regex("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")

    fun isLocalFile(): Boolean = !pageInfo.path.matches(URL_REGEX)
}

class Query(val cssSelector: String, override var name: String = "", val body: ElementBody) : ParentElement() {
    override fun toString(): String = "Query(name='$name', cssSelector='$cssSelector')"
}

class Container(override var name: String = "", val body: ElementBody) : ParentElement() {
    override fun toString(): String = "Wrapper(name='$name', body=$body)"
}

/* --------------------------------------------------- */
/* > Child Elements */
/* --------------------------------------------------- */

class Attr(override var name: String = "", val node: Node, val attrName: String) : SkrapeElemenet() {
    override fun toString(): String = "Attr(name='$name', nodes=$node, attrName='$attrName')"
}

class Value<T : Any>(override var name: String = "",
                     val node: Node,
                     val clazz: Class<T>) : SkrapeElemenet() {

    override fun toString(): String = "Text(name='$name', nodes=$node)"
}
