package com.example.roundtimer.data.local.model

import kotlinx.serialization.Serializable

@Serializable
data class QuoteCache(
    val date : String = "",
    val quote: String = "",
    val author: String = "",
)
