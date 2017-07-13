package nolambda.skrape.nodes

import java.io.File

interface Node {
    var name: String
}

sealed class KGElement : Node

abstract class ParentElement : KGElement() {
    val children = arrayListOf<KGElement>()
}

typealias ElementBody = ParentElement.() -> Unit

/* --------------------------------------------------- */
/* > Parent Elements */
/* --------------------------------------------------- */

class Page(val pageInfo: PageInfo, override var name: String = "", val body: ElementBody) : ParentElement() {

    constructor(path: String, baseUrl: String = "", body: ElementBody) : this(PageInfo(path, baseUrl), body = body)
    constructor(file: File, baseUrl: String = "", body: ElementBody) : this(file.path, baseUrl, body)

    override fun toString(): String = "Page(pageInfo=$pageInfo, name='$name', body=$body)"

    fun isLocalFile(): Boolean = pageInfo.baseUrl != pageInfo.path
}

class Query(val cssSelector: String, override var name: String = "", val body: ElementBody) : ParentElement() {
    override fun toString(): String = "Query(name='$name', cssSelector='$cssSelector')"
}

/* --------------------------------------------------- */
/* > Child Elements */
/* --------------------------------------------------- */

class Attr(override var name: String = "", val node: Node, val attrName: String) : KGElement() {
    override fun toString(): String = "Attr(name='$name', nodes=$node, attrName='$attrName')"
}

class Text(override var name: String = "", val node: Node) : KGElement() {
    override fun toString(): String = "Text(name='$name', nodes=$node)"
}
