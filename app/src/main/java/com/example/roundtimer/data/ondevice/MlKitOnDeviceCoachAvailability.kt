package com.example.roundtimer.data.ondevice

import com.example.roundtimer.domain.controller.OnDeviceCoachAvailability
import com.example.roundtimer.domain.model.OnDeviceCoachStatus
import com.google.mlkit.genai.common.DownloadStatus
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.prompt.Generation
import javax.inject.Inject

class MlKitOnDeviceCoachAvailability @Inject constructor() : OnDeviceCoachAvailability {
    override suspend fun getStatus(): OnDeviceCoachStatus {
        val model = Generation.getClient()

        return try {
            when (model.checkStatus()) {
                FeatureStatus.AVAILABLE ->
                    OnDeviceCoachStatus.AVAILABLE

                FeatureStatus.DOWNLOADABLE ->
                    OnDeviceCoachStatus.DOWNLOADABLE

                FeatureStatus.DOWNLOADING ->
                    OnDeviceCoachStatus.DOWNLOADING

                else ->
                    OnDeviceCoachStatus.UNAVAILABLE
            }
        } finally {
            model.close()
        }
    }

    override suspend fun downloadModel() {
        val model = Generation.getClient()

        try {
            model.download().collect { status ->
                if (status is DownloadStatus.DownloadFailed) {
                    throw status.e
                }
            }
        } finally {
            model.close()
        }
    }
}