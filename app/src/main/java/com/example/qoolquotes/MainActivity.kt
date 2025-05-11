package com.example.qoolquotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.qoolquotes.data.QuoteDao
import com.example.qoolquotes.data.QuoteDatabase
import com.example.qoolquotes.navigation.NavGraph
import com.example.qoolquotes.ui.theme.QoolQuotesTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var quoteDao: QuoteDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Initialize Room Database and QuoteDao
        quoteDao = QuoteDatabase.getDatabase(applicationContext).quoteDao()

        enableEdgeToEdge()
        setContent {
            MyApp(quoteDao)
        }
    }
}


@Composable
fun MyApp(quoteDao: QuoteDao) {

    QoolQuotesTheme {
        NavGraph(quoteDao = quoteDao)
    }
}


