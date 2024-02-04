package com.entropia.helpmepick.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entropia.helpmepick.AppViewModelProvider
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


    private val defaultDispatcher = Dispatchers.Default


    fun addItem(item: Item) = viewModelScope.launch(defaultDispatcher) {
        if (validateInput(item.name)) {
            itemsRepository.insertItem(item)
        }
    }


    fun selectItem(item: Item) {
        if (!isSelected(item)) {
            _itemsListUiState.value.selectedItemsList.add(item)
        }
        AppViewModelProvider.items=_itemsListUiState.value.selectedItemsList.toList()
    }

    fun deselectItem(item: Item) {
        if (isSelected(item)) {
            _itemsListUiState.value.selectedItemsList.remove(item)
        }
    }

    fun selectAll() {
        _itemsListUiState.value.itemsList.forEach { item ->
            selectItem(item)
        }
    }

    fun clearAll() {
        _itemsListUiState.value.selectedItemsList.removeAll(itemsListUiState.value.itemsList)
    }

    fun selectRandom(amountToSelect: Int) {
        if (_itemsListUiState.value.itemsList.isNotEmpty()) {
            val list: MutableList<Item> = _itemsListUiState.value.itemsList.toMutableList()
            clearAll()
            repeat(amountToSelect) {
                val item = list.random()
                selectItem(item)
                list.remove(item)
            }
        }
    }

    private fun isSelected(item: Item) =
        _itemsListUiState.value.selectedItemsList.contains(item)

    private fun validateInput(name: String): Boolean {
        return name.isNotBlank() && itemsRepository.getItem(name) == null
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}


data class ItemsListUiState(
    val itemsList: List<Item> = listOf(),
    val selectedItemsList: MutableList<Item> = mutableStateListOf(),
)