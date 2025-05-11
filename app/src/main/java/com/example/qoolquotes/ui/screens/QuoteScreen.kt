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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.qoolquotes.data.Quote
import com.example.qoolquotes.data.QuoteDao
import com.example.qoolquotes.navigation.BrowseScreenDestination
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.ui.components.MyBottomBar
import com.example.qoolquotes.ui.components.MyTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen(quoteDao: QuoteDao) {
    var quotes by remember { mutableStateOf<List<Quote>>(emptyList()) }
    var quoteCount by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var source by remember { mutableStateOf("") }

    // Collect quotes in a LaunchedEffect to update UI when data changes
    LaunchedEffect(Unit) {
        // This will collect the flow and update quotes
        quoteDao.getAllQuotes().collect { quoteList ->
            quotes = quoteList
        }
    }

    // Collect the quote count in a LaunchedEffect
    LaunchedEffect(Unit) {
        // Collect the count of quotes
        quoteDao.getAllQuoteCount().collect { count ->
            quoteCount = count  // Update the count
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopBar(title = "Dodaj cytat")
        }

    ) { innerPadding ->
    Column(Modifier.padding(innerPadding).padding(8.dp).fillMaxSize()) {

        // Input fields to add a new quote
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Treść") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(

            value = author,
            onValueChange = { author = it },
            label = { Text("Author") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        TextField(

            value = source,
            onValueChange = { source = it },
            label = { Text("Źródło") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Add quote button
        Button(
            onClick = {
                val newQuote = Quote(text = text, author = author, source = source, photoUri = null, audioUri = null)
                text = ""
                author = ""
                source = ""
                // Launch a coroutine to insert a quote
                CoroutineScope(Dispatchers.IO).launch {
                    quoteDao.insertQuote(newQuote)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Quote")
        }

        Text("Liczba cytatów: ${quoteCount}")
        // Display the list of quotes
        LazyColumn {
            items(quotes) { quote ->
                QuoteItem(quote, quoteDao)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }}
}

@Composable
fun QuoteItem(quote: Quote, quoteDao: QuoteDao) {
    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(quote.text)
                Text("- ${quote.author}, '${quote.source}'")
            }

            IconButton(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        quoteDao.delete(quote)
                    }
                }
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Back"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}