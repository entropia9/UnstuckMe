package com.entropia.helpmepick.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.extensions.isNotNull
import com.entropia.helpmepick.R
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository
import kotlinx.coroutines.launch

class PickRandomViewModel(private val itemsRepository: ItemsRepository, items: List<Item>) :
    ViewModel() {

    var pickItemUiState by mutableStateOf(PickItemUiState(selectedList = items))

    private var mutableSelectedList = pickItemUiState.selectedList.toMutableList()
    fun updateRejected() {
        if (pickItemUiState.currentPick.isNotNull()) {
            val timesRejected = pickItemUiState.currentPick!!.timesRejected + 1
            viewModelScope.launch {
                itemsRepository.updateItem(pickItemUiState.currentPick!!.copy(timesRejected = timesRejected))
            }
            mutableSelectedList.remove(pickItemUiState.currentPick)
            pickItemUiState = pickItemUiState.copy(
                selectedList = mutableSelectedList
            )
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
        } else {
            pickItemUiState =
                pickItemUiState.copy(currentPick = null)
        }
    }

    fun nextDialogue() {
        when {
            pickItemUiState.selectedList.isEmpty() -> pickItemUiState =
                pickItemUiState.copy(currentDialogue = pickItemUiState.outOfOptions)
        }
    }
}

data class PickItemUiState(
    val currentPick: Item? = null,
    val selectedList: List<Item>,
    val currentDialogue: Int = R.string.question_dialogue1,
    val questionDialogueList: List<Int> = listOf(
        R.string.question_dialogue2,
        R.string.question_dialogue3,
        R.string.question_dialogue4
    ),
    val pickedDialogue: Int = R.string.picked_dialogue,
    val outOfOptions: Int = R.string.out_of_options_dialogue
)

