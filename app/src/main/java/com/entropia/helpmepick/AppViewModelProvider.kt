package com.entropia.helpmepick

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.ui.battlemode.BattleModeViewModel
import com.entropia.helpmepick.ui.ItemsListViewModel
import com.entropia.helpmepick.ui.PickRandomViewModel
import com.entropia.helpmepick.ui.StatsViewModel

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
                items = items
            )
        }
        initializer {
            BattleModeViewModel(
                items = items
            )
        }
        initializer {
            StatsViewModel(
                itemsRepository = helpMePickApplication().container.itemsRepository
            )
        }
    }
}

fun CreationExtras.helpMePickApplication(): HelpMePickApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HelpMePickApplication)