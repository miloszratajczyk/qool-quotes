package com.example.qoolquotes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qoolquotes.data.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsDataStore = SettingsDataStore(application)

    val font = settingsDataStore.fontFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), "Default"
    )
    val darkTheme = settingsDataStore.darkThemeFlow.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), false
    )

    fun setFont(font: String) {
        viewModelScope.launch { settingsDataStore.setFont(font) }
    }
    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch { settingsDataStore.setDarkTheme(isDark) }
    }
}
