package com.example.roundtimer.ui.Screens.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roundtimer.domain.model.TimeSettings
import com.example.roundtimer.domain.usecase.QuoteUseCase
import com.example.roundtimer.domain.usecase.SavedTimerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val quoteUseCase: QuoteUseCase,
    private val savedTimerUseCase: SavedTimerUseCase
) : ViewModel() {

    private val _startUiState = MutableStateFlow(QuoteUiState())
    val stateUiState = _startUiState.asStateFlow()

    init {
        getQuoteForTheDay()
    }

    fun getQuoteForTheDay() {
        viewModelScope.launch {
            try {
                _startUiState.value = quoteUseCase.getQuoteForTheDay()?.let {
                    QuoteUiState(data = it.quote)
                } ?: run {
                    QuoteUiState(
                        data = "No quote for the day"
                    )
                }
            } catch (e : Exception) {
                _startUiState.value = QuoteUiState(
                    errorMessage = "Unable to load today's quote. Please try again"
                )
            }
        }
    }


    fun insertSavedTimer(
        timeSettings: TimeSettings,
        name : String
    ) {
        viewModelScope.launch {
            savedTimerUseCase.insertSavedTimer(
                timeSettings = timeSettings,
                name = name
            )
        }
    }
}