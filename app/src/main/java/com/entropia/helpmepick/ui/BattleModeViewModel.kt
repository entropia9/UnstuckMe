package com.entropia.helpmepick.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository

class BattleModeViewModel(private val itemsRepository: ItemsRepository, items: List<Item>) :
    ViewModel() {

    var battleModeUiState by mutableStateOf(
        BattleModeUiState(
            items = items,
            availableForSelection = items.toMutableList()
        )
    )



    fun getTwoItems() {
        val item1 = battleModeUiState.availableForSelection.random()
        battleModeUiState.availableForSelection.remove(item1)
        val item2 = battleModeUiState.availableForSelection.random()
        battleModeUiState = battleModeUiState.copy(
            item1 = item1,
            item2 = item2
        )
    }
    //TODO remove items
}

data class BattleModeUiState(
    val items: List<Item> = listOf(),
    var availableForSelection: MutableList<Item> = mutableListOf(),
    val item1: Item? = null,
    val item2: Item? = null
)