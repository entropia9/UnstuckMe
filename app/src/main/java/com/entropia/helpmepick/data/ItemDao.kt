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
    fun getItem(name: String):Item?

    @Query("SELECT * FROM items ORDER BY timesPicked DESC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM items ORDER BY timesSelected DESC")
    fun getAllItemsByTimesSelected():Flow<List<Item>>

    @Query("SELECT * FROM items ORDER BY timesRejected DESC")
    fun getAllItemsByTimesRejected():Flow<List<Item>>

    @Query("SELECT * FROM items WHERE timesSelected = 0")
    fun getNeverSelected():Flow<List<Item>>
}