package nolambda.skrape

import com.google.gson.annotations.SerializedName

data class HackerNewsResponse(
        @SerializedName("items") val stories: List<HackerNewsStory>
)