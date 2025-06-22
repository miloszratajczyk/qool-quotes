package com.example.qoolquotes.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.qoolquotes.data.Quote
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.navigation.QuoteScreenDestination
import com.example.qoolquotes.ui.components.AudioControlButton
import com.example.qoolquotes.ui.components.HelpButton
import com.example.qoolquotes.viewmodel.BrowseSoundsViewModel

@Composable
fun BrowseSoundsScreen(
    modifier: Modifier = Modifier, viewModel: BrowseSoundsViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val quotes by viewModel.audioQuotes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (quotes.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    "Nie znaleziono żadnych cytatów z dżwiękiem.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                HelpButton()
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()
            ) {
                items(quotes) { quote ->
                    Box(modifier = Modifier.clickable {
                        navController.navigate(QuoteScreenDestination(quote.id))
                    }) {
                        SoundQuoteItem(quote)
                    }
                }
            }
        }
    }
}

@Composable
fun SoundQuoteItem(quote: Quote) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp)
    ) {
        // Miniatura obrazka po lewej (jeśli istnieje)
        if (quote.photoUri != Uri.EMPTY) {
            AsyncImage(
                model = quote.photoUri,
                contentDescription = "quote image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Prawa strona: tekst i player audio
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = quote.text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "- ${quote.author}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Player audio po prawej
        AudioControlButton(uri = quote.audioUri)
    }
}
