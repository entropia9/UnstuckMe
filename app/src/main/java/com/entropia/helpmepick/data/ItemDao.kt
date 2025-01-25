package com.entropia.helpmepick.data

import androidx.room.ColumnInfo
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

    @Query("SELECT EXISTS (SELECT * FROM items WHERE name=:name COLLATE NOCASE)")
    fun getItem(name: String): Boolean

    @Query("SELECT * FROM items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE completed is 0 ORDER BY name ASC")
    fun getAllNotCompletedItems(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE completed is 1 ORDER BY name ASC")
    fun getCompleted(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE regularWins > 0 ORDER BY regularWins DESC")
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

    @Query("SELECT DISTINCT category FROM items WHERE completed is 0 ORDER BY name ASC")
    fun getAllCategories(): Flow<List<String>>

    @Query("SELECT * FROM items WHERE category=:category AND completed is 0")
    fun getItemsInCategory(category: String): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE category=:category")
    fun getAllItemsInCategory(category: String): Flow<List<Item>>

    @Query("SELECT category, COUNT(completed) FROM items WHERE completed=1 GROUP BY category ORDER BY COUNT(completed) DESC")
    fun getCategoriesByNumberOfCompletedItems(): Flow<List<CompletedTuple>>

    @Query("SELECT category, SUM(battleWins) FROM items WHERE battleWins>0 GROUP BY category ORDER BY SUM(battleWins) DESC")
    fun getCategoriesByNumberOfBattleWins(): Flow<List<BattleWinsTuple>>

    @Query("SELECT category, SUM(regularWins) FROM items WHERE regularWins>0 GROUP BY category ORDER BY SUM(regularWins) DESC")
    fun getCategoriesByNumberOfRegularWins(): Flow<List<RegularWinsTuple>>
}


data class CompletedTuple(
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "COUNT(completed)") val count: Int,
)

data class BattleWinsTuple(
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "SUM(battleWins)") val count: Int,
)

data class RegularWinsTuple(
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "SUM(regularWins)") val count: Int,
)