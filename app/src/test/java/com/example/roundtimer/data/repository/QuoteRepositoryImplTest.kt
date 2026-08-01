package com.example.roundtimer.data.repository

import com.example.roundtimer.BaseMockkTestClass
import com.example.roundtimer.data.local.datastore.QuoteCacheDataSource
import com.example.roundtimer.data.local.datastore.model.QuoteCache
import com.example.roundtimer.data.remote.QuoteApiService
import com.example.roundtimer.data.remote.model.QuoteDto
import com.example.roundtimer.domain.model.Quote
import com.example.roundtimer.utils.Utils
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class QuoteRepositoryImplTest : BaseMockkTestClass() {

    @MockK(relaxUnitFun = true)
    private lateinit var quoteApiService : QuoteApiService

    @MockK(relaxUnitFun = true)
    private lateinit var quoteCacheDataSource : QuoteCacheDataSource

    private lateinit var quoteRepositoryImpl: QuoteRepositoryImpl

    @Before
    override fun setUp() {
        super.setUp()
        quoteRepositoryImpl = QuoteRepositoryImpl(
            quoteApiService = quoteApiService,
            quoteCacheDataSource = quoteCacheDataSource
        )
    }
    @Test
    fun `getQuoteOfTheDay is not today quote`() = runTest {
        val quoteCache = QuoteCache(
            date = "2026-07-31",
            quote = "test",
            author = "Yu Hao"
        )
        val quoteDto = QuoteDto(
            quote = "test",
            author = "Yu Hao",
            date = "2026-07-31",
        )
        mockkObject(Utils)
        every {
            Utils.isToday(
                date = "2026-07-31"
            )
        } returns false
        coEvery {
            quoteCacheDataSource.getCache()
        } returns quoteCache
        coEvery {
            quoteApiService.getQuoteOfTheDay()
        } returns listOf(
            quoteDto
        )
        val result = quoteRepositoryImpl.getQuoteOfTheDay()
        Assert.assertEquals(
            quoteDto.toQuote(),
            result
        )
        coVerify(exactly = 1) {
            quoteApiService.getQuoteOfTheDay()
        }
        coVerify(exactly = 1) {
            quoteCacheDataSource.saveCache(
                quoteDto.toQuoteCache()
            )
        }
    }

    @Test
    fun `getQuoteOfTheDay quote cache is empty`() = runTest {
        val quoteCache = QuoteCache(
            date = "2026-07-31",
            quote = "",
            author = "Yu Hao"
        )
        val quoteDto = QuoteDto(
            quote = "test",
            author = "Yu Hao",
            date = "2026-07-31",
        )
        mockkObject(Utils)
        every {
            Utils.isToday(
                date = "2026-07-31"
            )
        } returns true
        coEvery {
            quoteCacheDataSource.getCache()
        } returns quoteCache
        coEvery {
            quoteApiService.getQuoteOfTheDay()
        } returns listOf(
            quoteDto
        )
        val result = quoteRepositoryImpl.getQuoteOfTheDay()
        Assert.assertEquals(
            quoteDto.toQuote(),
            result
        )
        coVerify(exactly = 1) {
            quoteApiService.getQuoteOfTheDay()
        }
        coVerify(exactly = 1) {
            quoteCacheDataSource.saveCache(
                quoteDto.toQuoteCache()
            )
        }
    }

    @Test
    fun `getQuoteOfTheDay quote from cache`() = runTest {
        val quoteCache = QuoteCache(
            date = "2026-07-31",
            quote = "test",
            author = "Yu Hao"
        )
        mockkObject(Utils)
        every {
            Utils.isToday(
                date = "2026-07-31"
            )
        } returns true
        coEvery {
            quoteCacheDataSource.getCache()
        } returns quoteCache
        val result = quoteRepositoryImpl.getQuoteOfTheDay()
        Assert.assertEquals(
            Quote(
                quote = quoteCache.quote,
                author = quoteCache.author
            ),
            result
        )
        coVerify(exactly = 0) {
            quoteApiService.getQuoteOfTheDay()
        }
        coVerify(exactly = 0) {
            quoteCacheDataSource.saveCache(
                any()
            )
        }
    }

    @Test
    fun `getQuoteOfTheDay failure case for remote`() = runTest {
        val quoteCache = QuoteCache(
            date = "2026-07-31",
            quote = "test",
            author = "Yu Hao"
        )

        mockkObject(Utils)

        every {
            Utils.isToday(quoteCache.date)
        } returns false

        coEvery {
            quoteCacheDataSource.getCache()
        } returns quoteCache

        coEvery {
            quoteApiService.getQuoteOfTheDay()
        } returns emptyList()

        val result = quoteRepositoryImpl.getQuoteOfTheDay()

        Assert.assertNull(result)

        coVerify(exactly = 1) {
            quoteApiService.getQuoteOfTheDay()
        }

        coVerify(exactly = 0) {
            quoteCacheDataSource.saveCache(any())
        }
    }
}