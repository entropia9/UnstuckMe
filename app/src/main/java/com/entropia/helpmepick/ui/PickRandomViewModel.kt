package com.entropia.helpmepick.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entropia.helpmepick.R
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository
import kotlinx.coroutines.launch


val questionDialogueList: List<Int> = listOf(
    R.string.question_dialogue2,
    R.string.question_dialogue3,
    R.string.question_dialogue4
)
val startAgainDialogue: Int = R.string.start_again_dialogue2
val pickedDialogue: Int = R.string.picked_dialogue
val outOfOptionsAgree: Int = R.string.out_of_options_agree
val outOfOptions: Int = R.string.out_of_options_dialogue

class PickRandomViewModel(private val itemsRepository: ItemsRepository, items: List<Item>) :
    ViewModel() {

    var pickItemUiState by mutableStateOf(PickItemUiState(selectedList = items))
        private set

    private var mutableSelectedList = pickItemUiState.selectedList.toMutableList()
    fun updateRejected() {
        if (pickItemUiState.currentPick != null) {
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
        if (pickItemUiState.currentPick != null) {
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
        pickItemUiState = if (items.isNotEmpty()) {
            pickItemUiState.copy(currentPick = items.random())
        } else {
            pickItemUiState.copy(currentPick = null)
        }
    }

    fun pickedDialogue() {
        pickItemUiState = when (pickItemUiState.currentDialogue) {
            outOfOptions -> pickItemUiState.copy(currentDialogue = outOfOptionsAgree)
            else -> pickItemUiState.copy(currentDialogue = pickedDialogue)
        }
    }

    fun nextDialogue() {
        pickItemUiState = when {
            pickItemUiState.selectedList.isEmpty() -> pickItemUiState.copy(currentDialogue = outOfOptions)

            pickItemUiState.currentDialogue == outOfOptions -> pickItemUiState.copy(currentDialogue = startAgainDialogue)

            else -> pickItemUiState.copy(currentDialogue = questionDialogueList.random())
        }
    }
}

data class PickItemUiState(
    val currentPick: Item? = null,
    val selectedList: List<Item>,
    val currentDialogue: Int = R.string.question_dialogue1
)

