package com.entropia.helpmepick.ui.regularmode

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


    private fun updateRejected() {
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

    private fun updatePicked() {
        if (pickItemUiState.currentPick != null) {
            val newItem =
                pickItemUiState.currentPick!!.copy(timesPicked = pickItemUiState.currentPick!!.timesPicked + 1)
            viewModelScope.launch {
                itemsRepository.updateItem(newItem)
            }
            pickItemUiState = pickItemUiState.copy(pickedItem = newItem)

        }
    }

    fun updateSelected(items: List<Item>) {
        val newList: MutableList<Item> = mutableListOf()
        items.forEach { item ->
            val newItem = item.copy(timesSelected = item.timesSelected + 1)
            viewModelScope.launch {
                itemsRepository.updateItem(newItem)
            }
            newList.add(newItem)
        }
        mutableSelectedList = newList
        pickItemUiState = pickItemUiState.copy(selectedList = newList)
    }

    fun pickRandomFromSelected(items: List<Item>) {
        pickItemUiState = if (items.isNotEmpty()) {
            pickItemUiState.copy(currentPick = items.random())
        } else {
            pickItemUiState.copy(currentPick = null)
        }
    }

    private fun showPickedDialogue() {
        pickItemUiState = when (pickItemUiState.currentDialogue) {
            outOfOptions -> pickItemUiState.copy(currentDialogue = outOfOptionsAgree)
            else -> pickItemUiState.copy(currentDialogue = pickedDialogue)
        }
    }


    fun updatePickedStatsAndShowDialogue(navigateUp: () -> Unit) {
        when (pickItemUiState.currentDialogue) {
            startAgainDialogue -> navigateUp()
            else -> {
                updatePicked()
                showPickedDialogue()
            }
        }
    }


    fun updateRejectedStatsAndShowDialogue(navigateUp: () -> Unit) {
        when (pickItemUiState.currentDialogue) {
            startAgainDialogue -> {
                navigateUp()
            }

            else -> {
                updateRejected()
                pickRandomFromSelected(pickItemUiState.selectedList)
                nextDialogue()
            }
        }
    }

    private fun nextDialogue() {
        pickItemUiState = when {
            pickItemUiState.selectedList.isEmpty() && pickItemUiState.currentDialogue != outOfOptions -> pickItemUiState.copy(
                currentDialogue = outOfOptions
            )

            pickItemUiState.currentDialogue == outOfOptions -> pickItemUiState.copy(currentDialogue = startAgainDialogue)

            else -> pickItemUiState.copy(currentDialogue = questionDialogueList.random())
        }
    }
}

data class PickItemUiState(
    val currentPick: Item? = null,
    val pickedItem: Item? = null,
    val selectedList: List<Item>,
    val currentDialogue: Int = R.string.question_dialogue1
)

