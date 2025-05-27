package com.example.qoolquotes.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import com.example.qoolquotes.data.SourceType
import com.example.qoolquotes.ui.components.AudioControlButton
import com.example.qoolquotes.ui.components.ImageFromUri
import com.example.qoolquotes.ui.components.SourceTypeDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuoteScreen(quoteDao: QuoteDao) {
    var quotes by remember { mutableStateOf<List<Quote>>(emptyList()) }
    var quoteCount by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var source by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf(Uri.EMPTY) }
    var audioUri by remember { mutableStateOf(Uri.EMPTY) }
    var sourceType by remember { mutableStateOf(SourceType.OTHER) }

    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            photoUri = uri
        }
    }
    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            audioUri = uri
        }
    }

    // Collect quotes in a LaunchedEffect to update UI when data changes
    LaunchedEffect(Unit) {
        // This will collect the flow and update quotes
        quoteDao.getAllQuotes().collect { quoteList ->
            quotes = quoteList
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopBar(
                title = "Dodaj cytat",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack
            )
        }

    ) { innerPadding ->
        Column(Modifier
            .padding(innerPadding)
            .padding(8.dp)
            .fillMaxSize()) {

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
            SourceTypeDropdown(
                selected = sourceType,
                onSelected = { sourceType = it }
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                if (photoUri != Uri.EMPTY) {
                    ImageFromUri(photoUri)
                }
            Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = {
                imagePickerLauncher.launch(arrayOf("image/*"))
            }) {
                Text("Wybierz zdjęcie dla cytatu")
            }


            }
            Spacer(modifier = Modifier.height(16.dp))
            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,)  {
                if (audioUri != Uri.EMPTY) {
                    AudioControlButton(audioUri)
                }

                Button(
                    onClick = {
                        audioPickerLauncher.launch(arrayOf("audio/*"))
                    }) {
                    Text("Wybierz audio dla cytatu")
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            // Add quote button
            Button(
                onClick = {
                    val newQuote = Quote(text = text, author = author, source = source, photoUri = photoUri, audioUri = audioUri, sourceType = sourceType)
                    text = ""
                    author = ""
                    source = ""
                    sourceType = SourceType.OTHER
                    photoUri = Uri.EMPTY
                    audioUri = Uri.EMPTY
                    // Launch a coroutine to insert a quote
                    CoroutineScope(Dispatchers.IO).launch {
                        quoteDao.insertQuote(newQuote)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Quote")
            }
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
    }
}