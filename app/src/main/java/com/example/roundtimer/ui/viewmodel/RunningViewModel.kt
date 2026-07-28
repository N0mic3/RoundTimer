package com.example.roundtimer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.roundtimer.model.Phase
import com.example.roundtimer.model.RoundInfoModel
import com.example.roundtimer.model.TimerUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RunningViewModel(
    val roundInfoModel: RoundInfoModel
) : ViewModel() {

    companion object {
        fun factory(
            roundInfoModel: RoundInfoModel
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                RunningViewModel(
                    roundInfoModel = roundInfoModel
                )
            }
        }
    }

    private val _timerUiState = MutableStateFlow(TimerUiState())
    val timerUiState = _timerUiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while(_timerUiState.value.isRunning
                && _timerUiState.value.phase != Phase.Complete) {
                delay(1000)
                onTimerTick()
            }
        }
    }

    private fun onTimerTick() {
        val timeState = _timerUiState.value
        if (timeState.secondLeft > 1) {
            _timerUiState.value = timeState.copy(
                secondLeft = timeState.secondLeft - 1
            )
        } else {
            _timerUiState.value = when(timeState.phase) {
                Phase.Ready -> {
                    timeState.copy(
                        phase = Phase.Work,
                        secondLeft = roundInfoModel.workDuration,
                    )
                }
                Phase.Work -> {
                    if (timeState.currentRoundIndex + 1 == roundInfoModel.roundCount) {
                        timeState.copy(
                            isRunning = false,
                            secondLeft = 0,
                            phase = Phase.Complete
                        )
                    } else {
                        timeState.copy(
                            secondLeft = roundInfoModel.restDuration,
                            phase = Phase.Rest,
                        )
                    }
                }
                Phase.Rest -> {
                    timeState.copy(
                        secondLeft = roundInfoModel.workDuration,
                        phase = Phase.Work,
                        currentRoundIndex = timeState.currentRoundIndex + 1
                    )
                }
                Phase.Complete -> timeState
            }
        }
    }

    fun onMainButtonClick() {
        if (timerUiState.value.phase == Phase.Complete) {
            resetTimer()
        } else {
            pauseTimer()
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _timerUiState.value = TimerUiState()
        startTimer()
    }

    fun pauseTimer() {
        _timerUiState.value = _timerUiState.value.copy(
            isRunning = !_timerUiState.value.isRunning
        )

        if (_timerUiState.value.isRunning) {
            startTimer()
        } else {
            timerJob?.cancel()
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}