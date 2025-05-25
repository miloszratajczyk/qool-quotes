package com.example.qoolquotes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.qoolquotes.navigation.BrowseScreenDestination
import com.example.qoolquotes.navigation.EditScreenDestination
import com.example.qoolquotes.navigation.AddQuoteScreenDestination
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
    var quoteWithImagesCount by remember { mutableStateOf(0) }
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
    LaunchedEffect(Unit) {
        // This will collect the flow and update quotes
        quoteDao.getQuotesWithImagesCount().collect { count ->
            quoteWithImagesCount = count
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopBar(
                title = "Najlepsze cytaty",
                hideBackButton = true,
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            )

        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ikona lupy po lewej
                    IconButton(
                        onClick = { navController.navigate(SearchScreenDestination) },
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Wyszukaj"
                        )
                    }

                    // Ikona plusa po prawej
                    IconButton(
                        onClick = {
                            navController.navigate(AddQuoteScreenDestination)
                        },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Dodaj"
                        )
                    }
                }
            }
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {

                Text(
                    text = "Twoje cytaty:\n",
                    fontSize = 24.sp,
                )

                // Statystyki cytatów
                Text(
                    buildAnnotatedString {
                        append("Znaleziono ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                            append("$quoteCount")
                        }
                        append(" cytatów")
                    },
                    fontSize = 16.sp
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(onClick = {
                        navController.navigate(
                            BrowseScreenDestination(selectedView = "texts")
                        )
                    },
                        modifier = Modifier.width(220.dp).padding(5.dp)) {
                        Text(text = "Wyświetl listę")
                    }
                }

                Text(
                    buildAnnotatedString {
                        append("Znaleziono ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                            append("$quoteWithImagesCount")
                        }
                        append(" cytatów z obrazami")
                    },
                    fontSize = 16.sp
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(onClick = {
                        navController.navigate(SlideshowScreenDestination)
                    },
                        modifier = Modifier.width(220.dp).padding(5.dp)) {
                        Text(text = "Wyświetl pokaz slajdów")
                    }
                }
            }

            Text(text = "For debug purposes", modifier = Modifier.padding(20.dp))

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
                navController.navigate(EditScreenDestination)
            }) {
                Text(text = "Go to EditScreenDestination")
            }

        }
    }
}