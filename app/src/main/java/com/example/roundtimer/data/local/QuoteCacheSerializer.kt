package com.example.roundtimer.data.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.roundtimer.data.local.model.QuoteCache
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object QuoteCacheSerializer : Serializer<QuoteCache> {
    override val defaultValue: QuoteCache = QuoteCache()

    override suspend fun readFrom(input: InputStream): QuoteCache {
        return try {
            Json.decodeFromString<QuoteCache>(
                input.readBytes().decodeToString()
            )

        } catch (e : SerializationException) {
            throw CorruptionException(
                "Unable to read quote cache",
                e
            )
        }
    }

    override suspend fun writeTo(
        t: QuoteCache,
        output: OutputStream
    ) {
        output.write(
            Json.encodeToString(t).encodeToByteArray()
        )
    }

}