package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Lifehack(
    @SerialName("content")
    val content: String,

    @SerialName("category_id")
    val categoryId: Long,

    @SerialName("rating")
    val rating: Double,

    @SerialName("rates_number")
    val ratesNumber: Int,
) : Model()