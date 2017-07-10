package nolambda.skrape

object SkrapeLogger {

    var enableLog = true

    fun log(message: String) {
        if (enableLog) {
            println(message)
        }
    }
}