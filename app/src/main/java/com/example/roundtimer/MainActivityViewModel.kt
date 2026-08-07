package com.example.roundtimer

import androidx.lifecycle.ViewModel
import com.example.roundtimer.domain.controller.TimerSessionController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val timerSessionController: TimerSessionController
) : ViewModel() {

    fun startTimerSession() = timerSessionController.startTimerSession()

    fun stopTimerSession() = timerSessionController.stopTimerSession()
}