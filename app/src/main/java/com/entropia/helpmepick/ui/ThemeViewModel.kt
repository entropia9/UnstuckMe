package com.entropia.helpmepick.ui

import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entropia.helpmepick.datastore.DataStoreManager
import com.entropia.helpmepick.datastore.DataStoreManager.Companion.IS_DARK_MODE_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ThemeViewModel(dataStoreManager: DataStoreManager) : ViewModel() {
    private val dataStore = dataStoreManager.dataStore

    private val _themeState = MutableStateFlow(ThemeState(false))
    val themeState: StateFlow<ThemeState> = _themeState

    private val dispatcher = Dispatchers.IO

    init {
        viewModelScope.launch(dispatcher) {
            dataStore.data.map { preferences ->
                ThemeState(preferences[IS_DARK_MODE_KEY] ?: false)
            }.collect {
                _themeState.value = it
                Log.d("storedData", _themeState.value.isDarkMode.toString())
            }
        }

    }

    fun getTheme(): Boolean {
        return _themeState.value.isDarkMode
    }

    fun setTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[IS_DARK_MODE_KEY] = isDarkMode
            }
        }
    }
}

data class ThemeState(val isDarkMode: Boolean)