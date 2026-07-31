package com.example.roundtimer.data.repository

import com.example.roundtimer.BaseMockkTestClass
import com.example.roundtimer.data.local.room.SavedTimerDao
import com.example.roundtimer.data.local.room.model.SavedTimerEntity
import com.example.roundtimer.domain.model.SavedTimer
import com.example.roundtimer.domain.model.TimeSettings
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SavedTimerRepositoryImplTest : BaseMockkTestClass() {

    @MockK(relaxUnitFun = true)
    lateinit var savedTimerDao: SavedTimerDao

    lateinit var savedTimerRepositoryImpl : SavedTimerRepositoryImpl

    @Before
    override fun setUp() {
        super.setUp()
        savedTimerRepositoryImpl = SavedTimerRepositoryImpl(
            savedTimerDao
        )
    }

    val savedTimer = SavedTimer(
        name = "test",
        timeSettings = TimeSettings(
            workDuration = 20,
            restDuration = 20,
            roundCount = 2
        )
    )

    val savedTimerEntity = SavedTimerEntity(
        name = savedTimer.name,
        workSeconds = savedTimer.timeSettings.workDuration,
        restSeconds = savedTimer.timeSettings.restDuration,
        roundCount = savedTimer.timeSettings.roundCount
    )

    @Test
    fun insertSavedTimeSettingTest() = runTest {
        savedTimerRepositoryImpl.insertSavedTimeSetting(
            timeSettings = savedTimer.timeSettings,
            name = savedTimer.name,
        )
        coVerify(exactly = 1) {
            savedTimerDao.insertSavedTimer(
                savedTimerEntity = savedTimerEntity
            )
        }
    }

    @Test
    fun updateSavedTimeSettingTest() = runTest {
        savedTimerRepositoryImpl.updateSavedTimeSetting(
            savedTimer = savedTimer
        )
        coVerify(exactly = 1) {
            savedTimerDao.updateSavedTimer(
                savedTimerEntity = savedTimerEntity
            )
        }
    }

    @Test
    fun deleteSavedTimeSettingTest() = runTest {
        savedTimerRepositoryImpl.deleteSavedTimeSetting(
            savedTimer = savedTimer
        )
        coVerify(exactly = 1) {
            savedTimerDao.deleteSavedTimer(
                savedTimerEntity = savedTimerEntity
            )
        }
    }

    @Test
    fun getSavedTimerListTest() = runTest {
        val savedTimerEntityList = listOf(
            savedTimerEntity
        )
        every {
            savedTimerDao.getSavedTimerList()
        } returns flowOf(savedTimerEntityList)
        val result = savedTimerRepositoryImpl.getSavedTimerList()
        verify(exactly = 1) {
            savedTimerDao.getSavedTimerList()
        }
        Assert.assertEquals(
            listOf(savedTimer),
            result.first()
        )
    }
}