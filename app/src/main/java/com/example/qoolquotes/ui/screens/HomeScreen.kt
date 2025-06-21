package com.example.qoolquotes.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.qoolquotes.navigation.*
import com.example.qoolquotes.ui.components.MyTopBar
import com.example.qoolquotes.viewmodel.HomeScreenViewModel
import com.example.qoolquotes.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    val navController = LocalNavController.current

    val quoteCount by viewModel.quoteCount.collectAsState()
    val quoteWithImagesCount by viewModel.quotesWithImagesCount.collectAsState()
    val quoteWithAudioCount by viewModel.quotesWithAudioCount.collectAsState()
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
                        onClick = { navController.navigate(SlideshowScreenDestination) },
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AutoStories, contentDescription = "Przeglądaj")
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

            randomQuote?.let { quote ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    if (quote.photoUri == Uri.EMPTY || quote.photoUri.toString().isBlank()) {
                        Image(
                            painter = painterResource(id = R.drawable.basic),
                            contentDescription = "Domyślne tło cytatu",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { navController.navigate(QuoteScreenDestination(quote.id)) }
                        )
                    } else {
                        AsyncImage(
                            model = quote.photoUri,
                            contentDescription = "Tło cytatu",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { navController.navigate(QuoteScreenDestination(quote.id)) }
                        )
                    }


                    // Ciemne tło z cytatem + autorem
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(
                                color = Color.Black.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(
                                    bottomStart = 16.dp,
                                    bottomEnd = 16.dp
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = quote.text.take(120) + if (quote.text.length > 120) "..." else "",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            if (quote.author.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp)) // mały odstęp
                                Text(
                                    text = "~ ${quote.author}",
                                    fontSize = 16.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            } ?: run {
                Text("Brak cytatów w bazie danych")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
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
                        .padding(vertical = 4.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Wyświetl wszystkie cytaty")
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
                    onClick = {
                        navController.navigate(BrowseScreenDestination(selectedView = "images"))
                    },
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Wyświetl cytaty z obrazem")
                }

                Text(
                    buildAnnotatedString {
                        append("Znaleziono ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                            append("$quoteWithAudioCount")
                        }
                        append(" cytatów z dżwiękiem")
                    },
                    fontSize = 16.sp
                )

                Button(
                    onClick = {
                        navController.navigate(BrowseScreenDestination(selectedView = "sounds")) },
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Wyświetl cytaty z dźwiękiem")
                }
            }


        }
    }
}
