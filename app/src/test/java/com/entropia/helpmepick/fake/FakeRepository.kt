package com.entropia.helpmepick.fake

import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class FakeRepository : ItemsRepository {
    var fakeData = FakeData()
    override fun getAllItemsStream(): Flow<List<Item>> {
        return listOf(fakeData.itemsList).asFlow()
    }

    override fun getItem(name: String): Item? {
        return fakeData.itemsList.firstOrNull { item: Item -> item.name == name }
    }

    override suspend fun insertItem(item: Item) {
        fakeData.itemsList.add(item)
    }

    override suspend fun deleteItem(item: Item) {
        fakeData.itemsList.remove(item)
    }

    override suspend fun updateItem(item: Item) {
        val index = fakeData.itemsList.indexOfFirst { oldItem -> oldItem.id == item.id }
        fakeData.itemsList[index] = item
    }

    override fun getAllByTimesPicked(): Flow<List<Item>> {
        return listOf(fakeData.itemsList.sortedBy { item -> item.timesPicked }).asFlow()
    }

    override fun getAllByTimesSelected(): Flow<List<Item>> {
        return listOf(fakeData.itemsList.sortedBy { item -> item.timesSelected }).asFlow()
    }

    override fun getAllByTimesRejected(): Flow<List<Item>> {
        return listOf(fakeData.itemsList.sortedBy { item -> item.timesRejected }).asFlow()
    }

    override fun getAllByBattleWins(): Flow<List<Item>> {
        return listOf(fakeData.itemsList.sortedBy { item -> item.battleWins }).asFlow()
    }

    override fun getNeverSelected(): Flow<List<Item>> {
        return listOf(fakeData.itemsList.filter { item: Item -> item.timesSelected == 0 }).asFlow()
    }

    override fun getCategories(): Flow<List<String>> {
        val list: List<String> = fakeData.itemsList.distinctBy { item: Item -> item.category }
            .map { item -> item.category }
        return listOf(list).asFlow()
    }

    override fun getItemsInCategory(category: String): Flow<List<Item>> {
        return listOf(fakeData.itemsList.filter { item: Item -> item.category == category }).asFlow()
    }

}