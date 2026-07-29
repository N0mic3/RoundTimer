package com.example.roundtimer.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.roundtimer.domain.usecase.QuoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val quoteUseCase: QuoteUseCase
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
}