package com.example.roundtimer.data.audio

import android.content.Context
import android.media.SoundPool
import com.example.roundtimer.R
import com.example.roundtimer.di.TimerSounds
import com.example.roundtimer.domain.model.TimerPhase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PhaseSoundPlayer @Inject constructor(
    @param:ApplicationContext private  val context: Context,
    @param:TimerSounds private val soundPool: SoundPool
) {

    private val workSoundId = soundPool.load(context, R.raw.work_start, 1)
    private val restSoundId = soundPool.load(context, R.raw.rest_start, 1)
    private val completeSoundId = soundPool.load(context, R.raw.timer_complete, 1)

    fun playFor(phase: TimerPhase) {
        val soundId = when(phase) {
            TimerPhase.Work -> workSoundId
            TimerPhase.Rest -> restSoundId
            TimerPhase.Complete -> completeSoundId
            TimerPhase.Ready -> return
        }

        soundPool.play(
            soundId,
            1f,
            1f,
            1,
            0,
            1f
        )
    }

}