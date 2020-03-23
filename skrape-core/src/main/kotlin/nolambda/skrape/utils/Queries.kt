package nolambda.skrape.utils

object Queries {
    fun indexOfChild(selector: String, index: Int) =
        "$selector:nth-child(${index})"
}