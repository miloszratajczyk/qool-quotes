package com.example.qoolquotes.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {
    companion object {
        val FONT_KEY = stringPreferencesKey("font")
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    }

    val fontFlow: Flow<String> = context.settingsDataStore.data
        .map { preferences -> preferences[FONT_KEY] ?: "Default" }

    val darkThemeFlow: Flow<Boolean> = context.settingsDataStore.data
        .map { preferences -> preferences[DARK_THEME_KEY] ?: false }

    suspend fun setFont(font: String) {
        context.settingsDataStore.edit { it[FONT_KEY] = font }
    }
    suspend fun setDarkTheme(isDark: Boolean) {
        context.settingsDataStore.edit { it[DARK_THEME_KEY] = isDark }
    }
}
