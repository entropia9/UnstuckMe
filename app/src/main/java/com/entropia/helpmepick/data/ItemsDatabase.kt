package com.entropia.helpmepick.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Item::class], version = 3, exportSchema = false)
abstract class ItemsDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: ItemsDatabase? = null
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'items' ADD COLUMN 'completed' INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): ItemsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    ItemsDatabase::class.java,
                    "item_database"
                ).addMigrations(MIGRATION_2_3).build().also {
                    Instance = it
                }
            }
        }
    }
}