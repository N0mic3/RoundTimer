package com.example.roundtimer.ui.start

import com.example.roundtimer.BaseMockkTestClass
import com.example.roundtimer.domain.model.Quote
import com.example.roundtimer.domain.model.TimeSettings
import com.example.roundtimer.domain.usecase.QuoteUseCase
import com.example.roundtimer.domain.usecase.SavedTimerUseCase
import com.example.roundtimer.testutil.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StartViewModelTest : BaseMockkTestClass() {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK(relaxUnitFun = true)
    lateinit var quoteUseCase: QuoteUseCase

    @MockK(relaxUnitFun = true)
    lateinit var savedTimerUseCase: SavedTimerUseCase

    private fun createViewModel(): StartViewModel {
        return StartViewModel(
            quoteUseCase = quoteUseCase,
            savedTimerUseCase = savedTimerUseCase
        )
    }

    @Test
    fun `init get quote for the day`() = runTest {
        val quote = Quote(
            quote = "hello",
            author = "me"
        )
        coEvery {
            quoteUseCase.getQuoteForTheDay()
        } returns quote
        val startViewModel = createViewModel()
        advanceUntilIdle()
        Assert.assertEquals(
            QuoteUiState(data = quote.quote),
            startViewModel.stateUiState.value
        )
        coVerify(exactly = 1) {
            quoteUseCase.getQuoteForTheDay()
        }
    }

    @Test
    fun `init get quote for the day null case`() = runTest {
        coEvery {
            quoteUseCase.getQuoteForTheDay()
        } returns null
        val startViewModel = createViewModel()
        advanceUntilIdle()
        Assert.assertEquals(
            QuoteUiState(data = "No quote for the day"),
            startViewModel.stateUiState.value
        )
        coVerify(exactly = 1) {
            quoteUseCase.getQuoteForTheDay()
        }
    }

    @Test
    fun `init get quote for the day crash case`() = runTest {
        coEvery {
            quoteUseCase.getQuoteForTheDay()
        } throws Exception("Failure")
        val startViewModel = createViewModel()
        advanceUntilIdle()
        Assert.assertEquals(
            QuoteUiState(
                errorMessage = "Unable to load today's quote. Please try again"
            ),
            startViewModel.stateUiState.value
        )
        coVerify(exactly = 1) {
            quoteUseCase.getQuoteForTheDay()
        }
    }

    @Test
    fun `insertSavedTimer value to use case`() = runTest {
        coEvery {
            quoteUseCase.getQuoteForTheDay()
        } returns null
        val timeSettings = TimeSettings(
            workDuration = 10,
            restDuration = 10,
            roundCount = 2
        )
        val name = "test"
        val startViewModel = createViewModel()
        startViewModel.insertSavedTimer(
            timeSettings = timeSettings,
            name = name
        )
        advanceUntilIdle()
        coVerify(exactly = 1) {
            savedTimerUseCase.insertSavedTimer(
                timeSettings = timeSettings,
                name = name
            )
        }
    }


}