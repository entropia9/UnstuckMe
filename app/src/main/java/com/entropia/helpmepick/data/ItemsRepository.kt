package com.entropia.helpmepick.data

import kotlinx.coroutines.flow.Flow


interface ItemsRepository {

    fun getAllItemsStream(): Flow<List<Item>>
    fun getAllNotCompletedItemsStream(): Flow<List<Item>>
    fun getCompleted(): Flow<List<Item>>

    fun getItem(name: String): Boolean

    suspend fun insertItem(item: Item)

    suspend fun deleteItem(item: Item)

    suspend fun updateItem(item: Item)

    fun getAllByTimesPicked(): Flow<List<Item>>
    fun getAllByTimesSelected(): Flow<List<Item>>
    fun getAllByTimesSelectedBattleMode(): Flow<List<Item>>

    fun getAllByTimesRejected(): Flow<List<Item>>
    fun getAllByTimesRejectedBattleMode(): Flow<List<Item>>

    fun getAllByBattleWins(): Flow<List<Item>>

    fun getNeverSelected(): Flow<List<Item>>

    fun getCategories(): Flow<List<String>>

    fun getItemsInCategory(category: String): Flow<List<Item>>
    fun getAllItemsInCategory(category: String): Flow<List<Item>>

    fun getCategoriesByNumberOfCompletedItems(): Flow<List<CompletedTuple>>

    fun getCategoriesByNumberOfBattleWins(): Flow<List<BattleWinsTuple>>

    fun getCategoriesByNumberOfRegularWins(): Flow<List<RegularWinsTuple>>
}
