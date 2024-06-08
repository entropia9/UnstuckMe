package com.entropia.helpmepick.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entropia.helpmepick.AppViewModelProvider
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemsListViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

    private val _itemsListUiState =
        itemsRepository.getAllItemsStream().map {
            ItemsListUiState(
                itemsList = it
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ItemsListUiState()
        )
    val itemsListUiState: StateFlow<ItemsListUiState> = _itemsListUiState
    val categoriesItemUiState: StateFlow<CategoryUiState> = MutableStateFlow(CategoryUiState())
    val addEditUiState: StateFlow<AddEditUiState> = MutableStateFlow(AddEditUiState())
    private val defaultDispatcher = Dispatchers.Default

    var editedItem: Pair<String, String> by mutableStateOf(Pair("", ""))
        private set

    init {
        viewModelScope.launch {
            (categoriesItemUiState as MutableStateFlow).value = CategoryUiState(
                itemsRepository.getCategories().first(),
                currentItems = itemsRepository.getAllItemsStream().first()
            )
        }

    }

    fun addItem(item: Item, dispatcher: CoroutineDispatcher = defaultDispatcher) =
        viewModelScope.launch(dispatcher) {
            if (validateInput(item.name)) {
                itemsRepository.insertItem(item)
                updateCategories(item, categoriesItemUiState)
            }
        }

    private suspend fun updateCategories(
        item: Item,
        categoriesItemUiState: StateFlow<CategoryUiState>
    ) {
        if (item.category != "" && !categoriesItemUiState.value.categories.contains(item.category)) {
            (categoriesItemUiState as MutableStateFlow).value = CategoryUiState(
                itemsRepository.getCategories().first(),
                categoriesItemUiState.value.currentCategory
            )

        }
        if (categoriesItemUiState.value.currentCategory == "") showAllItems() else showCurrentCategory(
            categoriesItemUiState.value.currentCategory
        )
    }

    fun updateItem(item: Item, dispatcher: CoroutineDispatcher = defaultDispatcher) =
        viewModelScope.launch(dispatcher) {
            itemsRepository.updateItem(item)
            updateCategories(item, categoriesItemUiState)
        }

    fun deleteItem(item: Item) = viewModelScope.launch {
        itemsRepository.deleteItem(item)
        (categoriesItemUiState as MutableStateFlow).value = CategoryUiState(
            itemsRepository.getCategories().first(),
            categoriesItemUiState.value.currentCategory
        )
        if (categoriesItemUiState.value.currentCategory == "") showAllItems() else showCurrentCategory(
            categoriesItemUiState.value.currentCategory
        )
    }

    fun selectItem(item: Item) {
        if (!isSelected(item)) {
            _itemsListUiState.value.selectedItemsList.add(item)
        }
        AppViewModelProvider.items = _itemsListUiState.value.selectedItemsList.toList()
    }

    fun deselectItem(item: Item) {
        if (isSelected(item)) {
            _itemsListUiState.value.selectedItemsList.remove(item)
        }
    }

    fun selectAll() {
        categoriesItemUiState.value.currentItems.forEach { item ->
            selectItem(item)
        }
    }

    fun clearAll() {
        _itemsListUiState.value.selectedItemsList.clear()
    }

    fun selectRandom(amountToSelect: Int) {
        if (_itemsListUiState.value.itemsList.isNotEmpty()) {
            val list: MutableList<Item> = categoriesItemUiState.value.currentItems.toMutableList()
            clearAll()
            repeat(amountToSelect) {
                val item = list.random()
                selectItem(item)
                list.remove(item)
            }
        }
    }

    fun showAllItems() {
        viewModelScope.launch {
            (categoriesItemUiState as MutableStateFlow).value = categoriesItemUiState.value.copy(
                currentCategory = "",
                currentItems = itemsRepository.getAllItemsStream().first()
            )
        }
    }


    fun showCurrentCategory(category: String) = viewModelScope.launch {

        (categoriesItemUiState as MutableStateFlow).value =
            categoriesItemUiState.value.copy(
                currentCategory = category,
                currentItems = itemsRepository.getItemsInCategory(category).first()
            )
    }


    fun updateEditedItem(name: String, category: String) {
        editedItem = Pair(name, category)

    }

    fun updateIsBeingEdited(isEdited: Boolean) {
        (addEditUiState as MutableStateFlow).value =
            addEditUiState.value.copy(
                isEdited = isEdited
            )
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
    val selectedItemsList: MutableList<Item> = mutableStateListOf()
)

data class CategoryUiState(
    val categories: List<String> = listOf(),
    val currentCategory: String = "",
    var currentItems: List<Item> = listOf()
)

data class AddEditUiState(
    val isEdited: Boolean = false
)