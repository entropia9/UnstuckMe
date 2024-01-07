package com.entropia.helpmepick.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemsListViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {
    val itemsListUiState = itemsRepository.getAllItemsStream().map {
        ItemsListUiState(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = ItemsListUiState()
    )

    fun addItem(item: Item) {
        viewModelScope.launch {
            if (validateInput(item.name)) {
                itemsRepository.insertItem(item)
            }
        }
    }

    private fun validateInput(name: String): Boolean {
        return name.isNotBlank() && itemsRepository.getItem(name) == null
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class ItemsListUiState(
    val itemsList: List<Item> = listOf()
)