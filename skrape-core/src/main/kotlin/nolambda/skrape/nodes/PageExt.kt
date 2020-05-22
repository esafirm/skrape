package nolambda.skrape.nodes

private val URL_REGEX = Regex("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")

fun Page.isLocalFile(): Boolean = !pageInfo.path.matches(URL_REGEX)

/**
 * Detect query without name
 * Ex: query("td") {
 *   ...
 * }
 *
 * This should result to JsonArray as the parent of the result rather than JsonObject
 * @return True if the if the parent container is Query and has no name or an empty Container
 */
fun Page.isUselessContainer(): Boolean {
    if (children.isEmpty()) return true
    if (children.size > 1) {
        return false
    }
    val firstChild = children.first()
    return firstChild is Query && firstChild.name.isBlank()
}