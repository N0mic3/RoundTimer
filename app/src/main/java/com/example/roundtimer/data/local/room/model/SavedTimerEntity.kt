package com.example.roundtimer.data.local.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.roundtimer.domain.model.SavedTimer
import com.example.roundtimer.domain.model.TimeSettings

@Entity(tableName = "saved_timers")
data class SavedTimerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name : String,
    val workSeconds: Int,
    val restSeconds: Int,
    val roundCount: Int
) {
    fun toSaveTimer() = SavedTimer(
        id = this.id,
        name = this.name,
        timeSettings = TimeSettings(
            workDuration = this.workSeconds,
            restDuration = this.restSeconds,
            roundCount = this.roundCount
        )
    )
}
