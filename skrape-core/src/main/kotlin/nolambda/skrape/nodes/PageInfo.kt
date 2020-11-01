package nolambda.skrape.nodes

import kotlinx.serialization.Serializable

@Serializable
data class PageInfo(
    val path: String,
    val baseUrl: String = "",
    val encoding: String = "UTF-8"
)