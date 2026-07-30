package com.example.roundtimer.ui.savedTimers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roundtimer.domain.model.SavedTimer
import com.example.roundtimer.domain.usecase.SavedTimerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedTimersViewModel @Inject constructor(
    private val savedTimerUseCase: SavedTimerUseCase
) : ViewModel() {

    init {
        getSavedTimeList()
    }

    private val _saveTimeUiState = MutableStateFlow(SavedTimersUiState())
    val saveTimeUiState = _saveTimeUiState.asStateFlow()

    fun getSavedTimeList() {
        viewModelScope.launch {
            savedTimerUseCase.getSavedTimerList().collect {
                _saveTimeUiState.value = _saveTimeUiState.value.copy(
                    savedTimeList = it
                )
            }
        }
    }

    fun updateSavedTimeList(
        savedTimer: SavedTimer
    ) {
        viewModelScope.launch {
            savedTimerUseCase.updateSavedTimer(
                savedTimer
            )
        }
    }

    fun deleteSavedTimeList(
        savedTimer: SavedTimer
    ) {
        viewModelScope.launch {
            savedTimerUseCase.deleteSavedTimerList(
                savedTimer
            )
        }
    }


}