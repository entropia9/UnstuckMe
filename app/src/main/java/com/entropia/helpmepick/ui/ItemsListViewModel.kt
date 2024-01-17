package com.entropia.helpmepick.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemsListViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {
    private val _itemsListUiState = itemsRepository.getAllItemsStream().map {
        ItemsListUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = ItemsListUiState()
    )
    val itemsListUiState: StateFlow<ItemsListUiState> = _itemsListUiState

    var pickItemUiState by mutableStateOf(PickItemUiState())

    private val defaultDispatcher = Dispatchers.Default


    fun addItem(item: Item) = viewModelScope.launch(defaultDispatcher) {
        if (validateInput(item.name)) {
            itemsRepository.insertItem(item)
        }
    }


    fun selectItem(item: Item) {
        _itemsListUiState.value.selectedItemsList.add(item)

        Log.d("SELECTED", _itemsListUiState.value.selectedItemsList.toString())
    }

    fun deselectItem(item: Item) {
        _itemsListUiState.value.selectedItemsList.remove(item)
    }

    fun listSelectedItems(): String {
        val names = _itemsListUiState.value.selectedItemsList.map { it.name }
        return names.joinToString(separator = ", ")
    }

    private fun validateInput(name: String): Boolean {
        return name.isNotBlank() && itemsRepository.getItem(name) == null
    }

    fun pickRandomFromSelected() {
        if (_itemsListUiState.value.selectedItemsList.isNotEmpty()) {
            pickItemUiState =
                pickItemUiState.copy(_itemsListUiState.value.selectedItemsList.random())
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class PickItemUiState(
    val currentPick: Item? = null
)

data class ItemsListUiState(
    val itemsList: List<Item> = listOf(),
    val selectedItemsList: MutableList<Item> = mutableStateListOf(),
)