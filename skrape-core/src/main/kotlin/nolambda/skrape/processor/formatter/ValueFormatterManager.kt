package nolambda.skrape.processor.formatter

import nolambda.skrape.nodes.Value

class ValueFormatterManager<ELEMENT, RETURN> : ValueFormatter<ELEMENT, RETURN> {

    val formatter: MutableList<ValueFormatter<ELEMENT, RETURN>> = mutableListOf()

    override fun format(value: Value<*>, element: ELEMENT): RETURN {
        formatter
                .filter { it.isForType(value) }
                .forEach { return it.format(value, element) }
        throw IllegalStateException("Should call isForType first for checking")
    }

    override fun isForType(value: Value<*>): Boolean = formatter.any { it.isForType(value) }

    fun addFormatter(formatter: ValueFormatter<ELEMENT, RETURN>) {
        this.formatter.add(formatter)
    }

    fun removeFormatter(formatter: ValueFormatter<ELEMENT, RETURN>) {
        this.formatter.remove(formatter)
    }
}