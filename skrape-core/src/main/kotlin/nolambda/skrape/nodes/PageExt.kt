package nolambda.skrape.nodes

private val URL_REGEX = Regex("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")

fun Page.isLocalFile(): Boolean = !pageInfo.path.matches(URL_REGEX)

fun Page.isUselessContainer(): Boolean {
    if (children.isEmpty()) return true
    if (children.size > 1) {
        return false
    }
    val firstChild = children.first()
    return firstChild is Query && firstChild.name.isBlank()
}