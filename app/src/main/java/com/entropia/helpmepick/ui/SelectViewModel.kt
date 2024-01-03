package com.entropia.helpmepick.ui

import androidx.lifecycle.ViewModel
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository

class SelectViewModel(val itemsRepository: ItemsRepository):ViewModel(){

}

data class SelectUiState(
    val itemsList: List<Item>,
    val selectedItems: List<Item>
)