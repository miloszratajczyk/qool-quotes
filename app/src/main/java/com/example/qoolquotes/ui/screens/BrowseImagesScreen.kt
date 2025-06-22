package com.example.qoolquotes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.qoolquotes.data.Quote
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.navigation.QuoteScreenDestination
import com.example.qoolquotes.ui.components.HelpButton
import com.example.qoolquotes.viewmodel.BrowseImagesViewModel

@Composable
fun BrowseImagesScreen(viewModel: BrowseImagesViewModel = hiltViewModel()) {
    val navController = LocalNavController.current
    val quotes by viewModel.imageQuotes.collectAsState()

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
                    "Nie znaleziono żadnych cytatów z obrazkiem.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                HelpButton()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(quotes) { quote ->
                    QuoteCard(quote = quote, onClick = {
                        navController.navigate(QuoteScreenDestination(quote.id))
                    })
                }
            }
        }
    }
}

@Composable
fun QuoteCard(quote: Quote, onClick: () -> Unit) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(12.dp))
        .clickable { onClick() }
        .fillMaxWidth()
        .aspectRatio(1f)) {
        quote.photoUri?.let { photoUri ->
            AsyncImage(
                model = photoUri,
                contentDescription = "Tło cytatu",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = painterResource(id = android.R.drawable.ic_menu_report_image)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = quote.text.take(60) + if (quote.text.length > 60) "..." else "",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color.White,
                modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(6.dp)
                    )
                    .padding(6.dp)
            )

            Text(
                text = quote.author, style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                ), color = Color.White, modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(6.dp)
                    )
                    .padding(6.dp)
            )
        }
    }
}
