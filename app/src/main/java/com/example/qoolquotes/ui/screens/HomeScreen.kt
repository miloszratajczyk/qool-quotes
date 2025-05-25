package com.example.qoolquotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.qoolquotes.navigation.*
import com.example.qoolquotes.ui.components.MyTopBar
import com.example.qoolquotes.viewmodel.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    val navController = LocalNavController.current

    val quoteCount by viewModel.quoteCount.collectAsState()
    val quoteWithImagesCount by viewModel.quotesWithImagesCount.collectAsState()
    val randomQuote by viewModel.randomQuote.collectAsState()

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
                    IconButton(
                        onClick = { navController.navigate(SearchScreenDestination) },
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Wyszukaj")
                    }

                    IconButton(
                        onClick = { navController.navigate(AddQuoteScreenDestination) },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Dodaj")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("LOSOWY CYTAT")

            randomQuote?.let { quote ->
                Text(
                    text = quote.text,
                    fontSize = 24.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (quote.author.isNotBlank()) {
                    Text(
                        text = "~ ${quote.author}",
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                if (!quote.source.isNullOrBlank()) {
                    Text(
                        text = "Źródło: ${quote.source}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                }
            } ?: run {
                Text("Brak cytatów w bazie danych")
            }


            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Text("Twoje cytaty:\n", fontSize = 24.sp)

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

                Button(
                    onClick = {
                        navController.navigate(BrowseScreenDestination(selectedView = "texts"))
                    },
                    modifier = Modifier
                        .width(220.dp)
                        .padding(5.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Wyświetl listę")
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

                Button(
                    onClick = { navController.navigate(SlideshowScreenDestination) },
                    modifier = Modifier
                        .width(220.dp)
                        .padding(5.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Wyświetl pokaz slajdów")
                }
            }

            Text("For debug purposes", modifier = Modifier.padding(20.dp))

            Button(onClick = {
                navController.navigate(BrowseScreenDestination(selectedView = "sounds"))
            }) {
                Text("Go to Browse sounds screen")
            }

            Button(onClick = {
                navController.navigate(EditScreenDestination)
            }) {
                Text("Go to EditScreenDestination")
            }
        }
    }
}
