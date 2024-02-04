package com.entropia.helpmepick

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.ui.ItemsListViewModel
import com.entropia.helpmepick.ui.PickRandomViewModel

object AppViewModelProvider {
    var items = listOf<Item>()
    val Factory = viewModelFactory {
        initializer {
            ItemsListViewModel(
                helpMePickApplication().container.itemsRepository
            )
        }
        initializer {
            PickRandomViewModel(
                itemsRepository = helpMePickApplication().container.itemsRepository,
                items=items
            )
        }
    }
}

fun CreationExtras.helpMePickApplication(): HelpMePickApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HelpMePickApplication)