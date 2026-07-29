package com.example.roundtimer.data.remote.model

import com.example.roundtimer.domain.model.Quote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteDto(
    @SerialName("q")
    val quote: String,
    @SerialName("a")
    val author: String,
) {
    fun toQuote() = Quote(
        quote = this.quote,
        author = this.author
    )
}