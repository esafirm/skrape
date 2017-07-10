package nolambda.kgdom

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

class Page(val url: String, override var name: String = "", val body: ElementBody) : ParentElement() {
    override fun toString(): String {
        return "Page(url='$url', name='$name')"
    }
}

class Query(val cssSelector: String, override var name: String = "", val body: ElementBody) : ParentElement() {
    override fun toString(): String {
        return "Query(name='$name', cssSelector='$cssSelector')"
    }
}

/* --------------------------------------------------- */
/* > Child Elements */
/* --------------------------------------------------- */

class Attr(override var name: String = "", val node: Node, val attrName: String) : KGElement() {
    override fun toString(): String = "Attr(name='$name', node=$node, attrName='$attrName')"
}

class Text(override var name: String = "", val node: Node) : KGElement() {
    override fun toString(): String {
        return "Text(name='$name', node=$node)"
    }
}
