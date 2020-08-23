package nolambda.skrape.processor.formatter

import nolambda.skrape.nodes.Value

interface ValueFormatter<in E, out R> {
    /**
     * Check if the formatter can handle the value
     */
    fun isForType(value: Value): Boolean
    fun format(value: Value, element: E): R
}