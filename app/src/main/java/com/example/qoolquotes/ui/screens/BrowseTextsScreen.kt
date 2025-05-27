package com.example.qoolquotes.ui.screens

import android.util.Log
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
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.font.FontWeight
import com.example.qoolquotes.navigation.AddQuoteScreenDestination
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.qoolquotes.navigation.QuoteScreenDestination
import com.example.qoolquotes.navigation.SearchScreenDestination
import androidx.hilt.navigation.compose.hiltViewModel
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
        LazyColumn {
            items(quotes) { quote ->
                QuoteItem(
                    quote = quote,
                    onClick = {
                        navController.navigate(QuoteScreenDestination(quote.id.toLong()))
                    },
                    onDelete = {
                        viewModel.deleteQuote(quote)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun QuoteItem(
    quote: Quote,
    onClick: () -> Unit,
    onDelete: () -> Unit
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
            .clickable { onClick() }
    ) {
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

            quote.photoUri?.let { photoUri ->
                AsyncImage(
                    model = photoUri.toString(),
                    contentDescription = "Zdjęcie cytatu",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 40.dp,
                                bottomStart = 40.dp
                            )
                        ),
                    error = painterResource(id = android.R.drawable.ic_menu_report_image)
                )
            }
        }

        Text(
            text = quote.sourceType.label,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Przycisk usuwania
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
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
