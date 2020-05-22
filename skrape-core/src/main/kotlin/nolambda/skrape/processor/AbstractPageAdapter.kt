package nolambda.skrape.processor

import nolambda.skrape.SkrapeLogger
import nolambda.skrape.nodes.*
import nolambda.skrape.processor.formatter.ValueFormatterManager
import nolambda.skrape.result.SkrapeResult

abstract class AbstractPageAdapter<ELEMENT, R, out T : SkrapeResult> : PageAdapter<T> {

    val formatterManager: ValueFormatterManager<ELEMENT, R> by lazy { ValueFormatterManager<ELEMENT, R>() }

    override fun adapt(page: Page): T {
        onStart()
        val requested = requestPage(page)
        val results = internalProcessPage(page, requested)
        val result = onHandleResult(page, results)
        onEnd()
        return result
    }

    open fun onStart() {}
    open fun onEnd() {}

    private fun internalProcessPage(page: Page, element: ELEMENT): List<R> = with(page) {
        evaluate()
        processChildren(page, element)
    }

    abstract fun requestPage(page: Page): ELEMENT

    abstract fun onHandleResult(page: Page, results: List<R>): T

    private fun processChildren(
        page: Page,
        element: ELEMENT
    ): List<R> = with(page) {
        children.map {
            processElement(it, element)
        }
    }

    abstract fun processQuery(query: Query, element: ELEMENT): R

    abstract fun processContainer(container: Container, element: ELEMENT): R

    abstract fun processAttr(attr: Attr, element: ELEMENT): R

    private fun processValue(value: Value<*>, element: ELEMENT): R = with(value) {
        return formatterManager.format(value, element)
    }

    protected fun processElement(skrapeElemenet: SkrapeElemenet, element: ELEMENT): R {
        SkrapeLogger.log("$skrapeElemenet")

        return when (skrapeElemenet) {
            is Query -> processQuery(skrapeElemenet, element)
            is Value<*> -> processValue(skrapeElemenet, element)
            is Attr -> processAttr(skrapeElemenet, element)
            is Container -> processContainer(skrapeElemenet, element)
            else -> throw IllegalStateException("Skrape Element undefined")
        }
    }

}