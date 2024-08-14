package com.entropia.helpmepick.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val category: String = "",
    val timesSelected: Int = 0,
    val timesSelectedBattleMode: Int = 0,
    val timesPicked: Int = 0,
    val timesRejected: Int = 0,
    val timesRejectedBattleMode: Int = 0,
    val battleWins: Int = 0,
    val completed: Boolean = false,
)
