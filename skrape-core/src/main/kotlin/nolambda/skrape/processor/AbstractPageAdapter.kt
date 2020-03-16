package nolambda.skrape.processor

import nolambda.skrape.processor.formatter.ValueFormatterManager

abstract class AbstractPageAdapter<E, R, out T> : PageAdapter<T> {

    val formatterManager: ValueFormatterManager<E, R> by lazy { ValueFormatterManager<E, R>() }
}