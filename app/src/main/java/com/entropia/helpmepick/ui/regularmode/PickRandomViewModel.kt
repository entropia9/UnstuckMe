package com.entropia.helpmepick.ui.regularmode

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entropia.helpmepick.AppViewModelProvider.items
import com.entropia.helpmepick.R
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository
import com.entropia.helpmepick.ui.outOfOptions
import com.entropia.helpmepick.ui.outOfOptionsAgree
import com.entropia.helpmepick.ui.pickedDialogue
import com.entropia.helpmepick.ui.questionDialogueList
import com.entropia.helpmepick.ui.startAgainDialogue
import kotlinx.coroutines.launch


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
                pickItemUiState.currentPick!!.copy(regularWins = pickItemUiState.currentPick!!.regularWins + 1)
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
            outOfOptions -> pickItemUiState.copy(finalText = outOfOptionsAgree)
            else -> pickItemUiState.copy(finalText = pickedDialogue)
        }
    }


    fun updatePickedStatsAndShowDialogue(navigateUp: () -> Unit) {
        when (pickItemUiState.currentDialogue) {
            startAgainDialogue -> navigateUp()
            else -> {
                updatePicked()
                showPickedDialogue()
                removeOneTime()
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
                if (pickItemUiState.selectedList.isEmpty()) {
                    removeOneTime()
                }
            }
        }
    }

    private fun removeOneTime() {
        viewModelScope.launch {
            items.forEach { item: Item ->
                if (item.oneTime) {
                    itemsRepository.deleteItem(item)
                }
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
    val currentDialogue: Int = R.string.question_dialogue1,
    val finalText: Int = 0,
)

