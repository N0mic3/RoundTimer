package com.example.roundtimer.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.roundtimer.data.local.room.model.SavedTimerEntity

@Database(
    entities = [SavedTimerEntity::class],
    version = 1,
)
abstract class RoundTimerDatabase : RoomDatabase() {
    abstract fun getSavedTimerDao() : SavedTimerDao

    companion object {
        const val DATABASE_NAME = "round_timer_database"
    }
}