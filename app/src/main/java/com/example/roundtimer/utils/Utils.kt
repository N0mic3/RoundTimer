package com.example.roundtimer.utils

import java.time.Clock
import java.time.LocalDate

object Utils {
    fun formatTime(totalSeconds: Int): String {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        return "%02d:%02d".format(minutes, seconds)
    }

    fun isToday(
        date: String,
        clock: Clock = Clock.systemDefaultZone()
    ): Boolean {
        return runCatching {
            LocalDate.parse(date) == LocalDate.now(clock)
        }.getOrDefault(false)
    }
}