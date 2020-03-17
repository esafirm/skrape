package nolambda.skrape.processor

import nolambda.skrape.nodes.SkrapeElemenet
import nolambda.skrape.processor.formatter.ValueFormatterManager
import nolambda.skrape.result.SkrapeResult

abstract class AbstractPageAdapter<E, R, out T : SkrapeResult> : PageAdapter<T> {

    val formatterManager: ValueFormatterManager<E, R> by lazy { ValueFormatterManager<E, R>() }
}