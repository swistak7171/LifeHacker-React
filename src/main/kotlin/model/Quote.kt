package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    @SerialName("_id")
    val id: String,

    @SerialName("content")
    val content: String,

    @SerialName("author")
    val author: String
)