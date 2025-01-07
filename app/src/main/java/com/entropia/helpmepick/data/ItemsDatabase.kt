package com.entropia.helpmepick.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Item::class], version = 6, exportSchema = false)
abstract class ItemsDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: ItemsDatabase? = null
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE 'items' ADD COLUMN 'completed' INTEGER NOT NULL DEFAULT 0")
            }
        }
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE 'items' ADD COLUMN 'oneTime' INTEGER NOT NULL DEFAULT 0")
            }
        }
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE 'items' ADD COLUMN 'timesSelectedRaffle' INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE 'items' ADD COLUMN 'timesRejectedRaffle' INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE 'items' ADD COLUMN 'raffleWins' INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE 'items' RENAME COLUMN 'timesPicked' TO 'regularWins'")

            }
        }

        fun getDatabase(context: Context): ItemsDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    ItemsDatabase::class.java,
                    "item_database"
                ).addMigrations(MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6).build().also {
                    Instance = it
                }
            }
        }
    }
}