package com.example.roundtimer.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteModel(
    @SerialName("q")
    val quote: String,
    @SerialName("a")
    val author: String,
)
