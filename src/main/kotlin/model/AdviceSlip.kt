package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdviceSlip(
    @SerialName("slip")
    val advice: Advice
)