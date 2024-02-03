package com.entropia.helpmepick.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository

class BattleModeViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

    var battleModeUiState by mutableStateOf(BattleModeUiState())

    fun pickRandomFromSelected(items: List<Item>) {
        if (items.isNotEmpty()) {
            battleModeUiState =
                battleModeUiState.copy(firstContestant = items.random())
            battleModeUiState = battleModeUiState.copy(secondContestant = items.random())

        }

    }


    //TODO remove items
}

data class BattleModeUiState(
    val firstContestant: Item? = null,
    val secondContestant: Item? = null
)