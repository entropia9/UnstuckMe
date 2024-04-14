package com.entropia.helpmepick.ui.test

import com.entropia.helpmepick.fake.FakeRepository
import com.entropia.helpmepick.rules.TestDispatcherRule
import com.entropia.helpmepick.ui.battlemode.BattleModeViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BattleModeViewModelTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @Test
    fun battleModeViewModel_onItemPick_newItemsSelected() = runTest {
        val repository = FakeRepository()
        val viewModel=BattleModeViewModel(repository.fakeData.itemsList, repository)
        val item1 = viewModel.battleModeUiState.item1
        val item2 = viewModel.battleModeUiState.item2
        if (item1 != null) {
            viewModel.onItemPick(item1)
        }
        assert(item1!=viewModel.battleModeUiState.item1
                && item2!=viewModel.battleModeUiState.item2
                && item1!=viewModel.battleModeUiState.item2
                && item2!=viewModel.battleModeUiState.item1)

    }

    @Test
    fun battleModeViewModel_onItemPick_battleWonAndStatsUpdated() = runTest {
        val repository = FakeRepository()
        val viewModel=BattleModeViewModel(repository.fakeData.itemsList, repository)
        var item1 = viewModel.battleModeUiState.item1
        if (item1 != null) {
            viewModel.onItemPick(item1)
        }
        item1 = viewModel.battleModeUiState.item1
        if (item1 != null) {
            viewModel.onItemPick(item1)
        }
        item1 = viewModel.battleModeUiState.item1
        if (item1 != null) {
            viewModel.onItemPick(item1)
        }
        assertEquals(item1, viewModel.battleModeUiState.winner)
        if (item1 != null) {
            assertEquals(1, repository.getItem(item1.name)?.battleWins ?: 0)
        }
    }
}