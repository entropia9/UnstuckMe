package com.entropia.helpmepick.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.extensions.isNotNull
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository
import kotlinx.coroutines.launch

class PickRandomViewModel(private val itemsRepository: ItemsRepository, items: List<Item>) :
    ViewModel() {

    var pickItemUiState by mutableStateOf(PickItemUiState(selectedList = items))


    fun updateRejected() {
        if (pickItemUiState.currentPick.isNotNull()) {
            val timesRejected = pickItemUiState.currentPick!!.timesRejected + 1
            viewModelScope.launch {
                itemsRepository.updateItem(pickItemUiState.currentPick!!.copy(timesRejected = timesRejected))
            }
        }
    }

    fun updatePicked() {
        if (pickItemUiState.currentPick.isNotNull()) {
            val timesPicked = pickItemUiState.currentPick!!.timesPicked + 1
            viewModelScope.launch {
                itemsRepository.updateItem(pickItemUiState.currentPick!!.copy(timesPicked = timesPicked))
            }
        }
    }

    fun updateSelected(items: List<Item>) {
        items.forEach { item ->
            val timesSelected = item.timesSelected + 1
            viewModelScope.launch {
                itemsRepository.updateItem(item.copy(timesSelected = timesSelected))
            }
        }
    }

    fun pickRandomFromSelected(items: List<Item>) {
        if (items.isNotEmpty()) {
            pickItemUiState =
                pickItemUiState.copy(currentPick = items.random())
        }
    }

}

data class PickItemUiState(
    val currentPick: Item? = null,
    var selectedList: List<Item>
)

