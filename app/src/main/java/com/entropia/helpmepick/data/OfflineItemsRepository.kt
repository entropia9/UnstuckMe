package com.entropia.helpmepick.data

import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: ItemDao) : ItemsRepository {
    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()
    override fun getAllNotCompletedItemsStream(): Flow<List<Item>> =
        itemDao.getAllNotCompletedItems()

    override fun getCompleted(): Flow<List<Item>> = itemDao.getCompleted()

    override fun getItem(name: String): Item? = itemDao.getItem(name)

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)
    override fun getAllByTimesPicked(): Flow<List<Item>> = itemDao.getAllItemsByTimesPicked()

    override fun getAllByTimesSelected(): Flow<List<Item>> = itemDao.getAllItemsByTimesSelected()
    override fun getAllByTimesSelectedBattleMode(): Flow<List<Item>> =
        itemDao.getAllItemsByTimesSelectedBattleMode()

    override fun getAllByTimesRejected(): Flow<List<Item>> = itemDao.getAllItemsByTimesRejected()
    override fun getAllByTimesRejectedBattleMode(): Flow<List<Item>> =
        itemDao.getAllItemsByTimesRejectedBattleMode()

    override fun getAllByBattleWins(): Flow<List<Item>> = itemDao.getAllItemsByBattleWins()

    override fun getNeverSelected(): Flow<List<Item>> = itemDao.getNeverSelected()
    override fun getCategories(): Flow<List<String>> = itemDao.getAllCategories()

    override fun getItemsInCategory(category: String): Flow<List<Item>> =
        itemDao.getItemsInCategory(category)

    override fun getAllItemsInCategory(category: String): Flow<List<Item>> =
        itemDao.getAllItemsInCategory(category)

    override fun getCategoriesByNumberOfCompletedItems(): Flow<List<CompletedTuple>> =
        itemDao.getCategoriesByNumberOfCompletedItems()

    override fun getCategoriesByNumberOfBattleWins(): Flow<List<BattleWinsTuple>> =
        itemDao.getCategoriesByNumberOfBattleWins()

    override fun getCategoriesByNumberOfRegularWins(): Flow<List<RegularWinsTuple>> =
        itemDao.getCategoriesByNumberOfRegularWins()

}