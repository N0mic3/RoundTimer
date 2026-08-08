package com.example.roundtimer.ui.savedTimers

import com.example.roundtimer.BaseMockkTestClass
import com.example.roundtimer.domain.model.SavedTimer
import com.example.roundtimer.domain.model.TimeSettings
import com.example.roundtimer.domain.usecase.SavedTimerUseCase
import com.example.roundtimer.testutil.MainDispatcherRule
import com.example.roundtimer.ui.Screens.savedTimers.SavedTimersViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SavedTimersViewModelTest : BaseMockkTestClass() {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK(relaxUnitFun = true)
    lateinit var savedTimerUseCase: SavedTimerUseCase

    private fun createViewModel(): SavedTimersViewModel {
        return SavedTimersViewModel(
            savedTimerUseCase = savedTimerUseCase
        )
    }

    val savedTimerList = listOf(SavedTimer(
        name = "test",
        timeSettings = TimeSettings(
            workDuration = 20,
            restDuration = 20,
            roundCount = 2
        )
    ))

    @Before
    override fun setUp() {
        super.setUp()
        coEvery {
            savedTimerUseCase.getSavedTimerList()
        } returns flowOf(savedTimerList)
    }

    @Test
    fun `init getSavedTimeList`() = runTest {
        val savedTimersViewModel = createViewModel()
        advanceUntilIdle()
        Assert.assertEquals(
            savedTimerList,
            savedTimersViewModel.saveTimeUiState.value.savedTimeList
        )
        coVerify(exactly = 1) {
            savedTimerUseCase.getSavedTimerList()
        }
    }

    @Test
    fun updateSavedTimeListTest() = runTest {
        val savedTimersViewModel = createViewModel()
        savedTimersViewModel.updateSavedTimeList(
            savedTimer = savedTimerList.first()
        )
        advanceUntilIdle()
        coVerify(exactly = 1) {
            savedTimerUseCase.updateSavedTimer(
                savedTimer = savedTimerList.first()
            )
        }
    }

    @Test
    fun deleteSavedTimerTest() = runTest {
        val savedTimersViewModel = createViewModel()
        savedTimersViewModel.deleteSavedTimer(
            savedTimer = savedTimerList.first()
        )
        advanceUntilIdle()
        coVerify(exactly = 1) {
            savedTimerUseCase.deleteSavedTimer(
                savedTimer = savedTimerList.first()
            )
        }
    }


}