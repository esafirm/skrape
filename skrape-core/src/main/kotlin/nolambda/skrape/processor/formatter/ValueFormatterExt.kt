package nolambda.skrape.processor.formatter

import nolambda.skrape.processor.AbstractPageAdapter

@Suppress("UNCHECKED_CAST")
fun <E, R, T> AbstractPageAdapter<E, R, T>.addFormatter(formatter: ValueFormatter<E, R>) {
    formatterManager.addFormatter(formatter)
}