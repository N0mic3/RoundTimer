package com.example.roundtimer.data.remote.model

import com.example.roundtimer.data.local.datastore.model.QuoteCache
import com.example.roundtimer.domain.model.Quote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteDto(
    @SerialName("q")
    val quote: String,
    @SerialName("a")
    val author: String,
    @SerialName("date")
    val date: String,
) {
    fun toQuote() = Quote(
        quote = this.quote,
        author = this.author
    )
    fun toQuoteCache() = QuoteCache(
        quote = this.quote,
        date = this.date,
        author = this.author
    )
}