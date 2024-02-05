package com.entropia.helpmepick.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.entropia.helpmepick.AppViewModelProvider.items
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository

class BattleModeViewModel(private val itemsRepository: ItemsRepository, items: List<Item>) :
    ViewModel() {

    var battleModeUiState by mutableStateOf(BattleModeUiState())


    fun assignItemsIntoPairs() {
        var mutableList = items
    }
    //TODO remove items
}

data class BattleModeUiState(
    val itemPairs: List<Pair<Item, Item>> = listOf()
)