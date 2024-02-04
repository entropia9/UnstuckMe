package com.entropia.helpmepick.ui

import com.entropia.helpmepick.R
import com.entropia.helpmepick.ui.navigation.NavigationDestination

object BattleModeDestination : NavigationDestination {
    override val route: String
        get() = "battle_mode"
    override val titleRes: Int
        get() = R.string.battle_mode

}