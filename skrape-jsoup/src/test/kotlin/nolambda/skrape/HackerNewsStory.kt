package nolambda.skrape

import com.google.gson.annotations.SerializedName

data class HackerNewsStory(
        @SerializedName("text") val title: String,
        @SerializedName("link") val uri: String
)