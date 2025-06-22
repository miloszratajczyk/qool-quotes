package com.example.qoolquotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.qoolquotes.data.Quote

@Composable
fun QuoteCard(quote: Quote, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.75f)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            // Top Image with text overlay
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = quote.photoUri,
                    contentDescription = "Quote background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent),
                                startY = 0f,
                                endY = 600f
                            )
                        )
                )

                AutoResizingText(
                    text = quote.text,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    color = Color.White,
                    maxLines = 15
                )
            }

            // Bottom info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = "Author")
                    Spacer(Modifier.width(4.dp))
                    Text(quote.author, style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(Modifier.height(8.dp))
                if (quote.source != null)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = sourceIcon(quote.sourceType),
                        contentDescription = "Source Type"
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(quote.source ?: "Nieznane", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
