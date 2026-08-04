package com.example.roundtimer.ui.running

import com.example.roundtimer.BaseMockkTestClass
import com.example.roundtimer.data.audio.PhaseSoundPlayer
import com.example.roundtimer.domain.model.TimeSettings
import com.example.roundtimer.domain.model.TimerPhase
import com.example.roundtimer.domain.usecase.TimeUseCase
import com.example.roundtimer.testutil.MainDispatcherRule
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RunningViewModelTest : BaseMockkTestClass() {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK(relaxUnitFun = true)
    lateinit var timeUseCase: TimeUseCase

    @MockK(relaxUnitFun = true)
    lateinit var phaseSoundPlayer: PhaseSoundPlayer

    val timeSettings = TimeSettings(
        workDuration = 20,
        restDuration = 20,
        roundCount = 2
    )

    private fun createViewModel(): RunningViewModel {
        return RunningViewModel(
            timeSettings = timeSettings,
            timeUseCase = timeUseCase,
            phaseSoundPlayer = phaseSoundPlayer
        )
    }

    @Test
    fun `init start timer`() = runTest {
        val currentState = TimerUiState().timeState
        val nextState = currentState.copy(
            secondsLeft = currentState.secondsLeft - 1
        )
        every {
            timeUseCase.getNextTimeState(
                currentState = currentState,
                timeSettings = timeSettings
            )
        } returns nextState
        val runningViewModel = createViewModel()
        advanceTimeBy(1000)
        runCurrent()
        Assert.assertEquals(
            nextState,
            runningViewModel.timerUiState.value.timeState
        )
        verify(exactly = 1) {
            timeUseCase.getNextTimeState(
                currentState = currentState,
                timeSettings = timeSettings
            )
        }
        verify(exactly = 0) {
            phaseSoundPlayer.playFor(
                any()
            )
        }
        runningViewModel.pauseTimer()
    }

    @Test
    fun `onMainButtonClick pause`() = runTest {
        val runningViewModel = createViewModel()
        runningViewModel.onMainButtonClick()
        val pausedState = runningViewModel.timerUiState.value.timeState

        advanceTimeBy(5_000)
        runCurrent()

        Assert.assertEquals(
            pausedState,
            runningViewModel.timerUiState.value.timeState
        )

        verify(exactly = 0) {
            timeUseCase.getNextTimeState(any(), any())
        }
    }

    @Test
    fun `onMainButtonClick resume`() = runTest {
        val currentState = TimerUiState().timeState
        val nextState = currentState.copy(
            secondsLeft = currentState.secondsLeft - 1
        )
        every {
            timeUseCase.getNextTimeState(
                currentState = currentState,
                timeSettings = timeSettings
            )
        } returns nextState
        val runningViewModel = createViewModel()
        runningViewModel.onMainButtonClick()
        runningViewModel.onMainButtonClick()
        advanceTimeBy(1_000)
        runCurrent()
        Assert.assertEquals(
            nextState,
            runningViewModel.timerUiState.value.timeState
        )
        verify(exactly = 1) {
            timeUseCase.getNextTimeState(
                currentState = currentState,
                timeSettings = timeSettings
            )
        }
        runningViewModel.pauseTimer()
    }

    @Test
    fun `onMainButtonClick reset`() = runTest {
        val startState = TimerUiState().timeState
        val nextState = startState.copy(
            phase = TimerPhase.Complete,
            secondsLeft = 0,
            isRunning = false
        )
        every {
            timeUseCase.getNextTimeState(
                currentState = startState,
                timeSettings = timeSettings
            )
        } returns nextState
        val runningViewModel = createViewModel()
        advanceTimeBy(1_000)
        runCurrent()
        verify(exactly = 1) {
            phaseSoundPlayer.playFor(
                TimerPhase.Complete
            )
        }
        Assert.assertEquals(
            nextState,
            runningViewModel.timerUiState.value.timeState
        )
        runningViewModel.onMainButtonClick()
        Assert.assertEquals(
            TimerUiState(),
            runningViewModel.timerUiState.value
        )
        runningViewModel.pauseTimer()
    }
}