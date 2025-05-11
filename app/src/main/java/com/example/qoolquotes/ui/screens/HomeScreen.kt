package com.example.qoolquotes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.qoolquotes.navigation.BrowseScreenDestination
import com.example.qoolquotes.navigation.EditScreenDestination
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.navigation.QuoteScreenDestination
import com.example.qoolquotes.navigation.SearchScreenDestination
import com.example.qoolquotes.navigation.SettingsScreenDestination
import com.example.qoolquotes.navigation.SlideshowScreenDestination
import com.example.qoolquotes.ui.components.MyBottomBar
import com.example.qoolquotes.ui.components.MyTopBar
import com.example.qoolquotes.data.Quote
import com.example.qoolquotes.data.QuoteDao
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen( modifier: Modifier = Modifier, quoteDao: QuoteDao) {
    val navController = LocalNavController.current

    var quoteCount by remember { mutableStateOf(0) }
    var randomQuote by remember { mutableStateOf<Quote?>(null) }

    // Pobieranie liczby cytatów
    LaunchedEffect(Unit) {
        quoteDao.getAllQuoteCount().collect { count ->
            quoteCount = count
        }
    }
    LaunchedEffect(Unit) {
        // This will collect the flow and update quotes
        quoteDao.getRandomQuote().collect { quote ->
            randomQuote = quote
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopBar(title = "Najlepsze cytaty", hideBackButton = true)
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("LOSOWY CYTAT")
            randomQuote?.let { quote ->
                // Wyświetlanie tekstu cytatu
                Text(
                    text = quote.text,
                    fontSize = 24.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Wyświetlanie autora
                if (quote.author.isNotBlank()) {
                    Text(
                        text = "~ ${quote.author}",
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Wyświetlanie źródła
                if (quote.source?.isNotBlank() == true) {
                    Text(
                        text = "Źródło: ${quote.source}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                }
            } ?: run {
                // Komunikat gdy nie ma cytatów
                Text("Brak cytatów w bazie danych")
            }


            // Statystyki cytatów
            Text("Znaleziono ${quoteCount} cytatów")

            Button(onClick = {
                navController.navigate(
                    BrowseScreenDestination(selectedView = "images")
                )
            }) {
                Text(text = "Go to Browse images screen")
            }
            Button(onClick = {
                navController.navigate(
                    BrowseScreenDestination(selectedView = "sounds")
                )
            }) {
                Text(text = "Go to Browse sounds screen")
            }
            Button(onClick = {
                navController.navigate(
                    BrowseScreenDestination(selectedView = "texts")
                )
            }) {
                Text(text = "Go to Browse texts screen")
            }
            Text(text = "For debug purpuses", modifier = Modifier.padding(32.dp))
            Button(onClick = {
                navController.navigate(SearchScreenDestination)
            }) {
                Text(text = "Go to SearchScreenDestination")
            }
            Button(onClick = {
                navController.navigate(EditScreenDestination)
            }) {
                Text(text = "Go to EditScreenDestination")
            }
            Button(onClick = {
                navController.navigate(QuoteScreenDestination)
            }) {
                Text(text = "Go to QuoteScreenDestination")
            }
            Button(onClick = {
                navController.navigate(SettingsScreenDestination)
            }) {
                Text(text = "Go to SettingsScreenDestination")
            }
            Button(onClick = {
                navController.navigate(SlideshowScreenDestination)
            }) {
                Text(text = "Go to SlideshowScreenDestination")
            }
        }
    }
}