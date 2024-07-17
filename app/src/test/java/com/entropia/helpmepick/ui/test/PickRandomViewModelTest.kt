package com.entropia.helpmepick.ui.test

import com.entropia.helpmepick.fake.FakeRepository
import com.entropia.helpmepick.rules.TestDispatcherRule
import com.entropia.helpmepick.ui.regularmode.PickRandomViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class PickRandomViewModelTest {


    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    val repository = FakeRepository()
    val viewModel = PickRandomViewModel(repository, repository.fakeData.itemsList)


    @Test
    fun pickRandomViewModel_updateSelected_updatedSelected() = runTest {
        viewModel.updateSelected(listOf(repository.fakeData.itemsList[0]))
        assertEquals(repository.fakeData.itemsList[0].timesSelected, 1)
    }

    @Test
    fun pickRandomViewModel_pickRandom_pickedRandom() = runTest {
        viewModel.pickRandomFromSelected(repository.fakeData.itemsList)
        assert(viewModel.pickItemUiState.currentPick !=null)
        viewModel.pickRandomFromSelected(emptyList())
        assert(viewModel.pickItemUiState.currentPick == null)
    }

    @Test
    fun pickRandomViewModel_updatePickedStatsAndShowDialogue_updatedPickedStats() = runTest {
        viewModel.pickRandomFromSelected(repository.fakeData.itemsList)
        viewModel.updatePickedStatsAndShowDialogue(navigateUp = {})
        assertEquals(1, viewModel.pickItemUiState.currentPick?.let { repository.getItem(it.name)?.timesPicked
            ?: 0 })
    }

    @Test
    fun pickRandomViewModel_updateRejectedStatsAndShowDialogue_updatedRejectedStats() = runTest {
        viewModel.pickRandomFromSelected(repository.fakeData.itemsList)
        val currentPick = viewModel.pickItemUiState.currentPick
        viewModel.updateRejectedStatsAndShowDialogue {  }
        if (currentPick != null) {
            assertEquals(1, repository.getItem(currentPick.name)?.timesRejected ?: 0)
        }
    }
}