package com.entropia.helpmepick.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 2, exportSchema = false)
abstract class ItemsDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: ItemsDatabase? = null

        fun getDatabase(context: Context): ItemsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    ItemsDatabase::class.java,
                    "item_database"
                ).fallbackToDestructiveMigration().build().also {
                    Instance = it
                }
            }
        }
    }
}