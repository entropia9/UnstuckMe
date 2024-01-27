package com.entropia.helpmepick.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository

class PickRandomViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {
    var progressStatus: PickingProgressStatus by mutableStateOf(PickingProgressStatus.Start)
        private set

    var pickItemUiState by mutableStateOf(PickItemUiState())

    fun updateItem(){

    }
    fun pickRandomFromSelected(items: List<Item>) {
        if (items.isNotEmpty()) {
            pickItemUiState =
                pickItemUiState.copy(currentPick = items.random())
        }
    }

}

data class PickItemUiState(
    val currentPick: Item? = null
)

sealed interface PickingProgressStatus {
    object Start : PickingProgressStatus
    object Picking : PickingProgressStatus

    object Finished : PickingProgressStatus
}

