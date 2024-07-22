package com.entropia.helpmepick.ui.battlemode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository
import kotlinx.coroutines.launch

class BattleModeViewModel(items: List<Item>, private val itemsRepository: ItemsRepository) :
    ViewModel() {

    var battleModeUiState by mutableStateOf(
        BattleModeUiState(
            items = items,
            availableForSelection = items.toMutableList()
        )
    )

    init {
        updateSelected(items)
        getTwoItems()
    }

    private fun updateSelected(items: List<Item>) {
        val newItems = mutableListOf<Item>()
        items.forEach { item ->
            val timesSelected = item.timesSelectedBattleMode + 1
            val newItem = item.copy(timesSelectedBattleMode = timesSelected)
            viewModelScope.launch {
                itemsRepository.updateItem(item.copy(timesSelectedBattleMode = timesSelected))
            }
            newItems.add(newItem)
        }
        battleModeUiState =
            battleModeUiState.copy(items = newItems, availableForSelection = newItems)
    }

    fun onItemPick(item: Item) {
        battleModeUiState.nextRoundList.add(item)
        if (battleModeUiState.availableForSelection.isNotEmpty()) {
            getTwoItems()
        } else {
            if (battleModeUiState.nextRoundList.size > 1) {
                battleModeUiState.availableForSelection = battleModeUiState.nextRoundList
                battleModeUiState.nextRoundList = mutableListOf()
                getTwoItems()
            } else {
                val newItem = item.copy(battleWins = item.battleWins + 1)
                viewModelScope.launch {
                    itemsRepository.updateItem(newItem)
                }
                battleModeUiState = battleModeUiState.copy(
                    winner = newItem
                )
            }
        }
    }

    fun updateRejected(item: Item) {
        val newItem = item.copy(timesRejectedBattleMode = item.timesRejectedBattleMode + 1)
        viewModelScope.launch {
            itemsRepository.updateItem(newItem)
        }
    }

    private fun getTwoItems() {
        val item1 = battleModeUiState.availableForSelection.random()
        battleModeUiState.availableForSelection.remove(item1)
        val item2 = battleModeUiState.availableForSelection.random()
        battleModeUiState.availableForSelection.remove(item2)
        battleModeUiState = battleModeUiState.copy(
            item1 = item1,
            item2 = item2,
            round = battleModeUiState.round + 1
        )
    }

}

data class BattleModeUiState(
    val round: Int = 0,
    val items: List<Item> = listOf(),
    var availableForSelection: MutableList<Item> = mutableListOf(),
    var nextRoundList: MutableList<Item> = mutableListOf(),
    val item1: Item? = null,
    val item2: Item? = null,
    val winner: Item? = null
)