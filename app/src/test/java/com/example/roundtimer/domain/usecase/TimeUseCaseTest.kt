package com.example.roundtimer.domain.usecase

import com.example.roundtimer.domain.model.TimeSettings
import com.example.roundtimer.domain.model.TimeState
import com.example.roundtimer.domain.model.TimerPhase
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TimeUseCaseTest {

    lateinit var timeUseCase: TimeUseCase

    @Before
    fun setUp() {
        timeUseCase = TimeUseCase()
    }

    val timeSettings = TimeSettings(
        workDuration = 30,
        restDuration = 20,
        roundCount = 4
    )

    @Test
    fun `getNextTimeState current second is greater 1`() {
        val timeState = TimeState(
            phase = TimerPhase.Ready,
            secondsLeft = 2,
            currentRoundIndex = 0
        )
        val result = timeUseCase.getNextTimeState(
            timeSettings = timeSettings,
            currentState = timeState
        )

        Assert.assertEquals(
            timeState.copy(
                secondsLeft = timeState.secondsLeft - 1
            ),
            result
        )
    }

    @Test
    fun `getNextTimeState ready phase to work`() {
        val timeState = TimeState(
            phase = TimerPhase.Ready,
            secondsLeft = 1,
            currentRoundIndex = 0
        )
        val result = timeUseCase.getNextTimeState(
            timeSettings = timeSettings,
            currentState = timeState
        )

        Assert.assertEquals(
            timeState.copy(
                phase = TimerPhase.Work,
                secondsLeft = timeSettings.workDuration
            ),
            result
        )
    }

    @Test
    fun `getNextTimeState work phase to rest`() {
        val timeState = TimeState(
            phase = TimerPhase.Work,
            secondsLeft = 1,
            currentRoundIndex = 0
        )
        val result = timeUseCase.getNextTimeState(
            timeSettings = timeSettings,
            currentState = timeState
        )

        Assert.assertEquals(
            timeState.copy(
                phase = TimerPhase.Rest,
                secondsLeft = timeSettings.restDuration
            ),
            result
        )
    }

    @Test
    fun `getNextTimeState work phase to done`() {
        val timeState = TimeState(
            phase = TimerPhase.Work,
            secondsLeft = 1,
            currentRoundIndex = 3
        )
        val result = timeUseCase.getNextTimeState(
            timeSettings = timeSettings,
            currentState = timeState
        )

        Assert.assertEquals(
            timeState.copy(
                phase = TimerPhase.Complete,
                secondsLeft = 0,
                isRunning = false
            ),
            result
        )
    }

    @Test
    fun `getNextTimeState rest phase to work`() {
        val timeState = TimeState(
            phase = TimerPhase.Rest,
            secondsLeft = 1,
            currentRoundIndex = 0
        )
        val result = timeUseCase.getNextTimeState(
            timeSettings = timeSettings,
            currentState = timeState
        )

        Assert.assertEquals(
            timeState.copy(
                phase = TimerPhase.Work,
                secondsLeft = timeSettings.workDuration,
                currentRoundIndex = timeState.currentRoundIndex + 1
            ),
            result
        )
    }

    @Test
    fun `getNextTimeState complete phase`() {
        val timeState = TimeState(
            phase = TimerPhase.Complete,
            secondsLeft = 0,
            currentRoundIndex = 4
        )
        val result = timeUseCase.getNextTimeState(
            timeSettings = timeSettings,
            currentState = timeState
        )

        Assert.assertEquals(
            timeState,
            result
        )
    }
}