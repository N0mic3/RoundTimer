package com.example.roundtimer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roundtimer.data.repository.QuoteRepository
import com.example.roundtimer.model.QuoteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StartViewModel : ViewModel() {
    private val quoteRepository = QuoteRepository()

    private val _startUiState = MutableStateFlow(QuoteUiState())
    val stateUiState = _startUiState.asStateFlow()

    init {
        getQuoteForTheDay()
    }

    fun getQuoteForTheDay() {
        viewModelScope.launch {
            try {
                _startUiState.value = quoteRepository.getQuoteOfTheDay()?.let {
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
}