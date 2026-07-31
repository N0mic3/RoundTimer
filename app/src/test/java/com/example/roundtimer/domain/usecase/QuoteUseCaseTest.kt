package com.example.roundtimer.domain.usecase

import com.example.roundtimer.domain.model.Quote
import com.example.roundtimer.domain.repository.QuoteRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class QuoteUseCaseTest {

    @MockK(relaxUnitFun = true)
    lateinit var quoteRepository: QuoteRepository

    lateinit var quoteUseCase: QuoteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        quoteUseCase = QuoteUseCase(quoteRepository)
    }

    @Test
    fun `getQuoteForTheDay delegates values to repository`() = runTest {
        val quote = Quote(
            quote = "test",
            author = "me"
        )
        coEvery {
            quoteRepository.getQuoteOfTheDay()
        } returns quote
        val result = quoteUseCase.getQuoteForTheDay()
        Assert.assertEquals(
            quote,
            result
        )
        coVerify(exactly = 1) {
            quoteRepository.getQuoteOfTheDay()
        }
    }

    @Test
    fun `getQuoteForTheDay delegates values to repository null case`() = runTest {
        coEvery {
            quoteRepository.getQuoteOfTheDay()
        } returns null
        val result = quoteUseCase.getQuoteForTheDay()
        Assert.assertEquals(
            null,
            result
        )
        coVerify(exactly = 1) {
            quoteRepository.getQuoteOfTheDay()
        }
    }
}