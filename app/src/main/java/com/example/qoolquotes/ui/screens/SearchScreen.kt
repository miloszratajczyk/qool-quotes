package com.example.qoolquotes.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.qoolquotes.navigation.BrowseScreenDestination
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.ui.components.MyBottomBar
import com.example.qoolquotes.ui.components.MyTopBar
import com.example.qoolquotes.viewmodel.SearchViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.room.util.query
import coil.compose.AsyncImage
import com.example.qoolquotes.ui.components.AudioControlButton
import com.example.qoolquotes.ui.components.ImageFromUri



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel = hiltViewModel()) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val quotes by viewModel.quotes.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .imePadding(),
        topBar = {
            MyTopBar(
                title = "Search",
                hideSettingsButton = true,
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,)
        },
         bottomBar = {
                RoundedSearchTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateQuery(it) }
                )
        },
        contentWindowInsets = WindowInsets.ime
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Adjust padding when keyboard is visible
        ) {

                items(quotes) { quote ->

                    ListItem(
                        leadingContent = {
                            if (quote.photoUri != Uri.EMPTY) {
                                AsyncImage(
                                    model = quote.photoUri,
                                    contentDescription = "quote image",
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            if (quote.audioUri != Uri.EMPTY) {
                                AudioControlButton(quote.audioUri)
                            }
                        },
                        overlineContent = {
                            Text(
                                if (quote.source != null)
                                    "${quote.author} - ${quote.source}" else
                                    quote.author,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        headlineContent = {
                            if (searchQuery != "") {
                                HighlightedText(
                                    fullText = "\"${quote.text}\" - ${quote.author}, ${quote.source}",
                                    query = searchQuery
                                )
                            } else {
                                Text(
                                    quote.text,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                        },
//                        supportingContent = {
//                            Text(quote.photoUri.toString())
//                        }

//                        supportingContent = {
//                            if (searchQuery != "") {
//                                Text(
//                                    quote.text,
//                                    maxLines = 2,
//                                    overflow = TextOverflow.Ellipsis
//                                )
//                            }
//                        }
                    )
                    HorizontalDivider()

                }
            }

        }

}
@Composable
fun HighlightedText(fullText: String, query: String, maxLength: Int = 64) {
    val annotatedString = remember(fullText, query) {
        val matchIndex = fullText.indexOf(query, ignoreCase = true)
        if (matchIndex == -1 || query.isBlank()) {
            return@remember buildAnnotatedString {
                append(fullText.take(maxLength))
                if (fullText.length > maxLength) append("…")
            }
        }

        // Calculate window to include query centered
        val halfWindow = (maxLength - query.length).coerceAtLeast(0) / 2
        val start = (matchIndex - halfWindow).coerceAtLeast(0)
        val end = (start + maxLength).coerceAtMost(fullText.length)

        val visibleText = fullText.substring(start, end)
        val visibleMatchStart = visibleText.indexOf(query, ignoreCase = true)

        buildAnnotatedString {
            if (start > 0) append("…")

            append(visibleText.substring(0, visibleMatchStart))

            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(visibleText.substring(visibleMatchStart, visibleMatchStart + query.length))
            }

            append(visibleText.substring(visibleMatchStart + query.length))

            if (end < fullText.length) append("…")
        }
    }

    Text(
        text = annotatedString,
        maxLines = 2,
        overflow = TextOverflow.Clip,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundedSearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Search..."
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        trailingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = TextFieldDefaults.colors()
    )
}
