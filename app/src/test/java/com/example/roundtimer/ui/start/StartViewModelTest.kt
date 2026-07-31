package com.example.roundtimer.ui.start

import com.example.roundtimer.domain.model.Quote
import com.example.roundtimer.domain.model.TimeSettings
import com.example.roundtimer.domain.usecase.QuoteUseCase
import com.example.roundtimer.domain.usecase.SavedTimerUseCase
import com.example.roundtimer.testutil.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StartViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK(relaxUnitFun = true)
    lateinit var quoteUseCase: QuoteUseCase

    @MockK(relaxUnitFun = true)
    lateinit var savedTimerUseCase: SavedTimerUseCase

    lateinit var startViewModelTest: StartViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        startViewModelTest = StartViewModel(
            quoteUseCase = quoteUseCase,
            savedTimerUseCase = savedTimerUseCase
        )
    }

    private fun createViewModel(): StartViewModel {
        return StartViewModel(
            quoteUseCase = quoteUseCase,
            savedTimerUseCase = savedTimerUseCase
        )
    }

    @Test
    fun `insertSavedTimer value to use case`() = runTest {
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