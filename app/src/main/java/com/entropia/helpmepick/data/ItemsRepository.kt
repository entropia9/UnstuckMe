package com.entropia.helpmepick.data

import kotlinx.coroutines.flow.Flow


interface ItemsRepository {

    fun getAllItemsStream(): Flow<List<Item>>


    fun getItem(name: String): Item?

    suspend fun insertItem(item: Item)

    suspend fun deleteItem(item: Item)

    suspend fun updateItem(item: Item)

}
