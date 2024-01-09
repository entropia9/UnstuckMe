package com.entropia.helpmepick

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.entropia.helpmepick.ui.ItemsListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ItemsListViewModel(
                helpMePickApplication().container.itemsRepository
            )
        }
    }
}

fun CreationExtras.helpMePickApplication(): HelpMePickApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HelpMePickApplication)