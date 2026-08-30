package com.example.roundtimer.domain.controller

import com.example.roundtimer.domain.model.OnDeviceCoachStatus

interface OnDeviceCoachAvailability {
    suspend fun getStatus(): OnDeviceCoachStatus

    suspend fun downloadModel()
}