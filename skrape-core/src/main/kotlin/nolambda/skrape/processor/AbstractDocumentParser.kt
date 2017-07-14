package nolambda.skrape.processor

import nolambda.skrape.processor.formatter.ValueFormatterManager

abstract class AbstractDocumentParser<E, R, out T> : DocumentParser<T> {

    val formatterManager: ValueFormatterManager<E, R> by lazy { ValueFormatterManager<E, R>() }

}