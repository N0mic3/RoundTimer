package com.example.roundtimer.domain.model

enum class OnDeviceCoachStatus(val displayMessage: String) {
    AVAILABLE("Available"),
    DOWNLOADABLE("Downloadable"),
    DOWNLOADING("Downloading"),
    UNAVAILABLE("Unavailable"),
}