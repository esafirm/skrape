package nolambda.kgdom

object KgLogger {

    var enableLog = true

    fun log(message: String) {
        if (enableLog) {
            println(message)
        }
    }
}