package com.example.roundtimer.ui.running

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.roundtimer.domain.model.TimeSettings
import com.example.roundtimer.domain.model.TimerPhase
import com.example.roundtimer.domain.usecase.TimeUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = RunningViewModel.Factory::class)
class RunningViewModel @AssistedInject constructor(
    @Assisted private val timeSettings: TimeSettings,
    private val timeUseCase: TimeUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(timeSettings : TimeSettings) : RunningViewModel
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
            while(_timerUiState.value.timeState.isRunning
                && _timerUiState.value.timeState.phase != TimerPhase.Complete) {
                delay(1000)
                onTimerTick()
            }
        }
    }

    private fun onTimerTick() {
        _timerUiState.value = _timerUiState.value.copy(
            timeState = timeUseCase.getNextTimeState(
                currentState = _timerUiState.value.timeState,
                timeSettings = timeSettings
            )
        )
    }

    fun onMainButtonClick() {
        if (timerUiState.value.timeState.phase == TimerPhase.Complete) {
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
            timeState = _timerUiState.value.timeState.copy(
                isRunning = !_timerUiState.value.timeState.isRunning
            )
        )

        if (_timerUiState.value.timeState.isRunning) {
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