package com.entropia.helpmepick.data

import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: ItemDao) : ItemsRepository {
    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    override fun getItem(name: String): Item? = itemDao.getItem(name)

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)
    override fun getAllByTimesSelected(): Flow<List<Item>> = itemDao.getAllItemsByTimesSelected()

    override fun getAllByTimesRejected(): Flow<List<Item>> = itemDao.getAllItemsByTimesRejected()
    override fun getNeverSelected(): Flow<List<Item>> = itemDao.getNeverSelected()

}