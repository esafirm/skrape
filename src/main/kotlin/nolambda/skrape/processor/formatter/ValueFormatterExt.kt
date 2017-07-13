package nolambda.skrape.processor.formatter

import nolambda.skrape.processor.AbstractDocumentParser

@Suppress("UNCHECKED_CAST")
fun <E, R, T> AbstractDocumentParser<E, R, T>.addFormatter(formatter: ValueFormatter<E, R>) {
    formatterManager.addFormatter(formatter)
}