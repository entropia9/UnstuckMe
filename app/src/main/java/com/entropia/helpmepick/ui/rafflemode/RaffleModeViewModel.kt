package com.entropia.helpmepick.ui.rafflemode

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
import com.entropia.helpmepick.ui.startAgainDialogue
import kotlinx.coroutines.launch

class RaffleModeViewModel(items: List<Item>, private val itemsRepository: ItemsRepository) :
    ViewModel() {

    var raffleUiState by mutableStateOf(RaffleUiState(selectedList = items))

    private var mutableSelectedList = raffleUiState.selectedList.toMutableList()

    fun updateSelected(items: List<Item>) {
        val newList: MutableList<Item> = mutableListOf()
        items.forEach { item ->
            val newItem = item.copy(timesSelectedRaffle = item.timesSelectedRaffle + 1)
            viewModelScope.launch {
                itemsRepository.updateItem(newItem)
            }
            newList.add(newItem)
        }
        mutableSelectedList = newList
        raffleUiState = raffleUiState.copy(selectedList = newList)
    }

    fun pickRandomFromSelected() {
        raffleUiState = if (items.isNotEmpty()) {
            raffleUiState.copy(currentPick = items.random())
        } else {
            raffleUiState.copy(currentPick = null)
        }
    }

    fun updateStatsAndShowDialogue(navigateToSelect: () -> Unit, navigateToMain: () -> Unit) {
        when (raffleUiState.currentDialogue) {
            startAgainDialogue -> {
                navigateToSelect()
                updateRejected()
            }

            outOfOptions -> {
                nextDialogue()
            }

            outOfOptionsAgree -> {
                navigateToMain()
            }

            else -> {
                updatePicked()
                showPickedDialogue()
            }
        }
    }

    fun nextDialogue() {
        raffleUiState = when (raffleUiState.currentDialogue) {
            startAgainDialogue -> raffleUiState.copy(currentDialogue = outOfOptions)
            outOfOptions -> raffleUiState.copy(currentDialogue = outOfOptionsAgree)
            else -> raffleUiState.copy(currentDialogue = startAgainDialogue)

        }
    }

    private fun showPickedDialogue() {
        raffleUiState = raffleUiState.copy(currentDialogue = pickedDialogue)
    }


    private fun updatePicked() {
        if (raffleUiState.currentPick != null) {
            val newItem =
                raffleUiState.currentPick!!.copy(raffleWins = raffleUiState.currentPick!!.raffleWins + 1)
            viewModelScope.launch {
                itemsRepository.updateItem(newItem)
            }
            raffleUiState = raffleUiState.copy(pickedItem = newItem)

        }
    }

    private fun updateRejected() {
        if (raffleUiState.currentPick != null) {
            val newItem =
                raffleUiState.currentPick!!.copy(timesRejectedRaffle = raffleUiState.currentPick!!.timesRejectedRaffle + 1)
            viewModelScope.launch {
                itemsRepository.updateItem(newItem)
            }
            raffleUiState = raffleUiState.copy(pickedItem = newItem)

        }
    }
}


data class RaffleUiState(
    val currentPick: Item? = null,
    val pickedItem: Item? = null,
    val selectedList: List<Item>,
    val currentDialogue: Int = R.string.question_dialogue1,
)