package com.example.roundtimer.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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

    @Update
    suspend fun updateSavedTimer(
        savedTimerEntity: SavedTimerEntity
    ) : Int

    @Delete
    suspend fun deleteSavedTimer(
        savedTimerEntity: SavedTimerEntity
    ): Int
}