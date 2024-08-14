package com.entropia.helpmepick.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)
    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM items WHERE name=:name COLLATE NOCASE")
    fun getItem(name: String): Item?

    @Query("SELECT * FROM items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>
    @Query("SELECT * FROM items WHERE completed is 0 ORDER BY name ASC")
    fun getAllNotCompletedItems(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE timesPicked > 0 ORDER BY timesPicked DESC")
    fun getAllItemsByTimesPicked(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE timesSelected > 0 ORDER BY timesSelected DESC")
    fun getAllItemsByTimesSelected(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE timesSelectedBattleMode > 0 ORDER BY timesSelectedBattleMode DESC")
    fun getAllItemsByTimesSelectedBattleMode(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE timesRejected > 0 ORDER BY timesRejected DESC")
    fun getAllItemsByTimesRejected(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE timesRejectedBattleMode > 0 ORDER BY timesRejectedBattleMode DESC")
    fun getAllItemsByTimesRejectedBattleMode(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE battleWins > 0 ORDER BY battleWins DESC")
    fun getAllItemsByBattleWins(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE timesSelected = 0")
    fun getNeverSelected(): Flow<List<Item>>

    @Query("SELECT DISTINCT category FROM items ORDER BY name")
    fun getAllCategories(): Flow<List<String>>

    @Query("SELECT * FROM items WHERE category=:category")
    fun getItemsInCategory(category: String):Flow<List<Item>>

}