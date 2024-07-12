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

    init {
        getMostSelected()
        getMostSelectedBattleMode()
        getMostPicked()
        getMostRejected()
        getMostRejectedBattleMode()
        getMostBattleWins()

    }

    private fun getMostRejectedBattleMode() {
        viewModelScope.launch {
            getAllByTimesRejectedBattleMode()
        }
    }

    private fun getMostSelectedBattleMode() {
        viewModelScope.launch {
            getAllByTimesSelectedBattleMode()
        }
    }

    private fun getMostPicked() {
        viewModelScope.launch {
            getAllByTimesPicked()
        }
    }

    private fun getMostSelected() {
        viewModelScope.launch {
            getAllByTimesSelected()
        }
    }

    private fun getMostBattleWins() {
        viewModelScope.launch {
            getAllByBattleWins()
        }
    }

    private fun getMostRejected() {
        viewModelScope.launch {
            getAllByTimesRejected()
        }
    }

    private suspend fun getAllByTimesPicked() {
        val mostPickedDeferred = viewModelScope.async {
            itemsRepository.getAllByTimesPicked().first()
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

    private suspend fun getAllByTimesSelectedBattleMode() {
        val mostSelectedDeferred = viewModelScope.async {
            itemsRepository.getAllByTimesSelectedBattleMode().first()
        }
        val mostSelected = mostSelectedDeferred.await()
        statsUiState = statsUiState.copy(
            mostSelectedBattleMode = mostSelected
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

    private suspend fun getAllByTimesRejectedBattleMode() {
        val mostRejectedDeferred = viewModelScope.async {
            itemsRepository.getAllByTimesRejectedBattleMode().first()
        }
        val mostRejected = mostRejectedDeferred.await()
        statsUiState = statsUiState.copy(
            mostRejectedBattleMode = mostRejected
        )
    }

    private suspend fun getAllByBattleWins() {
        val mostBattleWinsDeferred = viewModelScope.async {
            itemsRepository.getAllByBattleWins().first()
        }
        val mostBattleWins = mostBattleWinsDeferred.await()
        statsUiState = statsUiState.copy(
            mostBattleWins = mostBattleWins
        )
    }
}

data class StatsUiState(
    val mostPicked: List<Item> = listOf(),
    val mostSelected: List<Item> = listOf(),
    val mostSelectedBattleMode: List<Item> = listOf(),
    val mostRejected: List<Item> = listOf(),
    val mostRejectedBattleMode: List<Item> = listOf(),
    val mostBattleWins: List<Item> = listOf()
)

