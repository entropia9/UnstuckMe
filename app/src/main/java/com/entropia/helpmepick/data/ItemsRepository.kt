package com.entropia.helpmepick.data

import kotlinx.coroutines.flow.Flow


interface ItemsRepository {

    fun getAllItemsStream(): Flow<List<Item>>


    fun getItem(name: String): Item?

    suspend fun insertItem(item: Item)

    suspend fun deleteItem(item: Item)

    suspend fun updateItem(item: Item)

    fun getAllByTimesPicked(): Flow<List<Item>>
    fun getAllByTimesSelected(): Flow<List<Item>>

    fun getAllByTimesRejected(): Flow<List<Item>>

    fun getAllByBattleWins(): Flow<List<Item>>

    fun getNeverSelected(): Flow<List<Item>>

    fun getCategories(): Flow<List<String>>

    fun getItemsInCategory(category: String): Flow<List<Item>>
}
