package com.example.roundtimer.domain.usecase

import com.example.roundtimer.domain.model.TimeSettings
import com.example.roundtimer.domain.repository.SavedTimerRepository
import javax.inject.Inject

class SavedTimerUseCase @Inject constructor(
    private val savedTimerRepository: SavedTimerRepository
) {
    suspend fun insertSavedTimer(
        timeSettings: TimeSettings,
        name : String
    ) = savedTimerRepository.insertSavedTimeSetting(
        timeSettings = timeSettings,
        name = name
    )

    fun getSavedTimerList() = savedTimerRepository.getSavedTimerList()
}