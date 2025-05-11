package com.example.qoolquotes.ui.screens

import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.font.FontWeight

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
            MyTopBar(title = "Lista cytatów")
        }

    ) { innerPadding ->
    Column(Modifier.padding(innerPadding).padding(8.dp).fillMaxSize()) {

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
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        // Pierwsza linia - autor i źródło pogrubione
        Row {
            if (quote.author.isNotBlank()) {
                Text(
                    text = "${quote.author},",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            if (quote.source?.isNotBlank() == true) {
                Text(
                    text = "'${quote.source}'",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }


        }

        // Nowa linia - treść cytatu
        Text(
            text = quote.text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Test wyświetlania rodzaju źródła
        Text(
            text = quote.sourceType.label,
            style = MaterialTheme.typography.bodySmall
        )

        // Przycisk usuwania wyrównany do prawej
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        quoteDao.delete(quote)
                    }
                }
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Usuń cytat",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}