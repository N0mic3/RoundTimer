package com.example.roundtimer.data.repository

import com.example.roundtimer.data.local.room.SavedTimerDao
import com.example.roundtimer.data.local.room.model.SavedTimerEntity
import com.example.roundtimer.domain.model.SavedTimer
import com.example.roundtimer.domain.model.TimeSettings
import com.example.roundtimer.domain.repository.SavedTimerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SavedTimerRepositoryImpl @Inject constructor(
    private val savedTimerDao: SavedTimerDao
) : SavedTimerRepository{
    override suspend fun insertSavedTimeSetting(
        timeSettings: TimeSettings,
        name: String
    ) {
        savedTimerDao.insertSavedTimer(
            SavedTimerEntity(
                name = name,
                workSeconds = timeSettings.workDuration,
                restSeconds = timeSettings.restDuration,
                roundCount = timeSettings.roundCount,
            )
        )
    }

    override suspend fun updateSavedTimeSetting(
        savedTimer: SavedTimer
    ) {
        savedTimerDao.updateSavedTimer(
            SavedTimerEntity(
                id = savedTimer.id,
                name = savedTimer.name,
                workSeconds = savedTimer.timeSettings.workDuration,
                restSeconds = savedTimer.timeSettings.restDuration,
                roundCount = savedTimer.timeSettings.roundCount,
            )
        )
    }

    override suspend fun deleteSavedTimeSetting(
        savedTimer: SavedTimer
    ) {
        savedTimerDao.deleteSavedTimer(
            SavedTimerEntity(
                id = savedTimer.id,
                name = savedTimer.name,
                workSeconds = savedTimer.timeSettings.workDuration,
                restSeconds = savedTimer.timeSettings.restDuration,
                roundCount = savedTimer.timeSettings.roundCount,
            )
        )
    }

    override fun getSavedTimerList(): Flow<List<SavedTimer>> = savedTimerDao.getSavedTimerList().map { it ->
        it.map {
            it.toSaveTimer()
        }
    }

}