package com.example.roundtimer.domain.usecase

import com.example.roundtimer.domain.model.TimeSettings
import com.example.roundtimer.domain.model.TimeState
import com.example.roundtimer.domain.model.TimerPhase

class TimeUseCase {
    fun getNextTimeState(
        currentState: TimeState,
        timeSettings: TimeSettings
    ) : TimeState {
        return if (currentState.secondsLeft > 1) {
             currentState.copy(
                secondsLeft = currentState.secondsLeft - 1
            )
        } else {
            when(currentState.phase) {
                TimerPhase.Ready -> {
                    currentState.copy(
                        phase = TimerPhase.Work,
                        secondsLeft = timeSettings.workDuration,
                    )
                }
                TimerPhase.Work -> {
                    if (currentState.currentRoundIndex + 1 == timeSettings.roundCount) {
                        currentState.copy(
                            isRunning = false,
                            secondsLeft = 0,
                            phase = TimerPhase.Complete
                        )
                    } else {
                        currentState.copy(
                            secondsLeft = timeSettings.restDuration,
                            phase = TimerPhase.Rest,
                        )
                    }
                }
                TimerPhase.Rest -> {
                    currentState.copy(
                        secondsLeft = timeSettings.workDuration,
                        phase = TimerPhase.Work,
                        currentRoundIndex = currentState.currentRoundIndex + 1
                    )
                }
                TimerPhase.Complete -> currentState
            }
        }
    }
}