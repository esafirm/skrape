package nolambda.skrape.processor.formatter

import nolambda.skrape.processor.AbstractPageAdapter
import nolambda.skrape.result.SkrapeResult

@Suppress("UNCHECKED_CAST")
fun <E, R, T: SkrapeResult> AbstractPageAdapter<E, R, T>.addFormatter(formatter: ValueFormatter<E, R>) {
    formatterManager.addFormatter(formatter)
}