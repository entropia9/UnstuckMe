package com.entropia.helpmepick.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class StatsViewModel(val itemsRepository: ItemsRepository) : ViewModel() {

    var statsUiState by mutableStateOf(StatsUiState())
        private set


    fun getMostPicked() {
        viewModelScope.launch {
            getAllByTimesPicked()
        }
    }

    fun getMostSelected() {
        viewModelScope.launch {
            getAllByTimesSelected()
        }
    }

    private suspend fun getAllByTimesPicked() {
        val mostPickedDeferred = viewModelScope.async {
            itemsRepository.getAllItemsStream().first()
        }
        val mostPicked = mostPickedDeferred.await()
        statsUiState = statsUiState.copy(
            mostPicked = mostPicked
        )
    }

    private suspend fun getAllByTimesSelected() {
        val mostSelectedDeferred = viewModelScope.async {
            itemsRepository.getAllByTimesSelected().first()
        }
        val mostSelected = mostSelectedDeferred.await()
        statsUiState = statsUiState.copy(
            mostSelected = mostSelected
        )
    }

    private suspend fun getAllByTimesRejected() {
        val mostRejectedDeferred = viewModelScope.async {
            itemsRepository.getAllByTimesRejected().first()
        }
        val mostRejected = mostRejectedDeferred.await()
        statsUiState = statsUiState.copy(
            mostRejected = mostRejected
        )
    }


}

data class StatsUiState(
    val mostPicked: List<Item> = listOf(),
    val mostSelected: List<Item> = listOf(),
    val mostRejected: List<Item> = listOf(),
)

