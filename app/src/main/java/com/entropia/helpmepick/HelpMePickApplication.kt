package com.entropia.helpmepick

import android.app.Application
import com.entropia.helpmepick.data.AppContainer
import com.entropia.helpmepick.data.AppDataContainer

class HelpMePickApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}