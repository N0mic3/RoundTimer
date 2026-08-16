package com.example.roundtimer.domain.controller

interface FocusReminderScheduler {

    fun scheduleDailyReminder()

    fun cancelDailyReminder()
}