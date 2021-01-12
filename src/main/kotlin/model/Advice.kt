package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Advice(
    @SerialName("id")
    val id: Long,

    @SerialName("advice")
    val content: String
)