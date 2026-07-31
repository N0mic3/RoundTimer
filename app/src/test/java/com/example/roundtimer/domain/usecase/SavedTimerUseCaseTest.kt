package com.example.roundtimer.domain.usecase

import com.example.roundtimer.domain.model.SavedTimer
import com.example.roundtimer.domain.model.TimeSettings
import com.example.roundtimer.domain.repository.SavedTimerRepository
import io.mockk.MockKAnnotations
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

class SavedTimerUseCaseTest {

    @MockK(relaxUnitFun = true)
    lateinit var savedTimerRepository: SavedTimerRepository

    lateinit var savedTimerUseCase: SavedTimerUseCase

    val timeSettings = TimeSettings(
        workDuration = 10,
        restDuration = 10,
        roundCount = 3
    )

    val savedTimer = SavedTimer(
        timeSettings = timeSettings,
        name = "test"
    )
    val name = "test"
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        savedTimerUseCase = SavedTimerUseCase(
            savedTimerRepository = savedTimerRepository
        )
    }

    @Test
    fun `testInsertSavedTimer delegates values to repository`() = runTest {
        savedTimerUseCase.insertSavedTimer(
            timeSettings = timeSettings,
            name = name
        )
        coVerify(exactly = 1) {
            savedTimerRepository.insertSavedTimeSetting(
                timeSettings = timeSettings,
                name = name
            )
        }
    }
    @Test
    fun `testUpdateSavedTimer delegates values to repository`() = runTest {
        savedTimerUseCase.updateSavedTimer(
            savedTimer = savedTimer
        )
        coVerify(exactly = 1) {
            savedTimerRepository.updateSavedTimeSetting(
                savedTimer = savedTimer
            )
        }
    }
    @Test
    fun `testDeleteSavedTimer delegates values to repository`() = runTest {
        savedTimerUseCase.deleteSavedTimer(
            savedTimer = savedTimer
        )
        coVerify(exactly = 1) {
            savedTimerRepository.deleteSavedTimeSetting(
                savedTimer = savedTimer
            )
        }
    }

    @Test
    fun `testGetSavedTimerList delegates values to repository`() = runTest {
        val savedTimerList = listOf(
            savedTimer
        )
        every {
            savedTimerRepository.getSavedTimerList()
        } returns flowOf(savedTimerList)
        val result = savedTimerUseCase.getSavedTimerList().first()
        Assert.assertEquals(
            savedTimerList,
            result
        )
        verify(exactly = 1) {
            savedTimerRepository.getSavedTimerList()
        }
    }
}