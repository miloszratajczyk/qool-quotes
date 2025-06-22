package com.example.qoolquotes.ui.screens

import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.qoolquotes.R
import com.example.qoolquotes.data.Quote
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.navigation.QuoteScreenDestination
import com.example.qoolquotes.ui.components.HelpButton
import com.example.qoolquotes.viewmodel.BrowseTextsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseTextsScreen(viewModel: BrowseTextsViewModel = hiltViewModel()) {
    val navController = LocalNavController.current
    val quotes by viewModel.allQuotes.collectAsState()

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        if (quotes.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    "Nie znaleziono żadnych cytatów.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                HelpButton()
            }
        } else {
            LazyColumn {
                items(quotes) { quote ->
                    QuoteItem(quote = quote, onClick = {
                        navController.navigate(QuoteScreenDestination(quote.id))
                    }, onDelete = {
                        viewModel.deleteQuote(quote)
                    })
                }
            }
        }
    }
}

@Composable
fun QuoteItem(
    quote: Quote, onClick: () -> Unit, onDelete: () -> Unit
) {
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
            .clickable { onClick() }) {
        // Autor + źródło
        Text(
            text = "${quote.author}, ${quote.source}",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(end = 8.dp)
        )

        // Cytat i zdjęcie obok siebie
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = quote.text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )


            if (!(quote.photoUri == Uri.EMPTY || quote.photoUri.toString().isBlank())) {
                AsyncImage(
                    model = quote.photoUri.toString(),
                    contentDescription = "Zdjęcie cytatu",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 40.dp, bottomStart = 40.dp
                            )
                        ),
                    error = painterResource(id = R.drawable.basic)
                )
            }
        }


        // Przycisk usuwania
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = quote.sourceType.label,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Usuń cytat",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
