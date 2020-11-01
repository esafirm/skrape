package nolambda.skrape

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HackerNewsResponse(
    @SerialName("items") val stories: List<HackerNewsStory>
)

@Serializable
data class HackerNewsStory(
    @SerialName("text") val title: String,
    @SerialName("detail") val detail: StoryDetail
)

@Serializable
data class StoryDetail(
    @SerialName("link") val uri: String
)