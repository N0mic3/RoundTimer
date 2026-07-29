package com.example.roundtimer.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.roundtimer.data.local.room.model.SavedTimerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedTimerDao {
    @Insert
    suspend fun insertSavedTimer(
        savedTimerEntity: SavedTimerEntity
    ) : Long

    @Query(
        "SELECT * FROM saved_timers ORDER BY ID DESC"
    )
    fun getSavedTimerList() : Flow<List<SavedTimerEntity>>
}