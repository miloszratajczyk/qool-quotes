package com.example.qoolquotes

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.qoolquotes.data.QuoteDao
import com.example.qoolquotes.navigation.NavGraph
import com.example.qoolquotes.ui.theme.QoolQuotesTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

@HiltAndroidApp
class MyApp : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var quoteDao: QuoteDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            // Możesz ustawić domyślnie true/false lub isSystemInDarkTheme()

            QoolQuotesTheme(darkTheme = isDarkTheme) {
                NavGraph(
                    quoteDao = quoteDao,
                    onChangeTheme = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}
