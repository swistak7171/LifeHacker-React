package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LifehackRequestBody(
    @SerialName("content")
    val content: String,

    @SerialName("category_id")
    val categoryId: Long,
)