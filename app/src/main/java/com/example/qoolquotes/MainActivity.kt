package com.example.qoolquotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qoolquotes.data.QuoteDao
import com.example.qoolquotes.navigation.NavGraph
import com.example.qoolquotes.ui.theme.QoolQuotesTheme
import com.example.qoolquotes.ui.theme.getTypographyForFont // <-- DODANO!
import com.example.qoolquotes.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

@HiltAndroidApp
class MyApp : android.app.Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var quoteDao: QuoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val isDarkTheme by settingsViewModel.darkTheme.collectAsState()
            val selectedFont by settingsViewModel.font.collectAsState()

            QoolQuotesTheme(
                darkTheme = isDarkTheme,
                typography = getTypographyForFont(selectedFont) // <-- TU!
            ) {
                NavGraph(
                    quoteDao = quoteDao,
                    onChangeTheme = { settingsViewModel.setDarkTheme(!isDarkTheme) },
                    selectedFont = selectedFont,
                    onFontChange = { settingsViewModel.setFont(it) }
                )
            }
        }
    }
}
