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

    @Query("SELECT * FROM items WHERE name=:name")
    fun getItem(name: String): Flow<Item>

    @Query("SELECT * FROM items ORDER BY timesPicked DESC")
    fun getAllItems(): Flow<List<Item>>
}