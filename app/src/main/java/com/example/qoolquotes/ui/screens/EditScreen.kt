package com.example.qoolquotes.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.qoolquotes.data.Quote
import com.example.qoolquotes.data.QuoteDao
import com.example.qoolquotes.data.SourceType
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.ui.components.ImageFromUri
import com.example.qoolquotes.ui.components.AudioControlButton
import com.example.qoolquotes.ui.components.SourceTypeDropdown
import com.example.qoolquotes.ui.components.MyTopBar
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import android.provider.OpenableColumns

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    quoteId: Int? = null,
    modifier: Modifier = Modifier,
    quoteDao: QuoteDao? = null
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Stany pól edycji – domyślnie puste
    var text by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var source by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    var audioUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    var sourceType by remember { mutableStateOf(SourceType.OTHER) }
    var initialized by remember { mutableStateOf(false) }

    // Pobierz cytat i uzupełnij pola (tylko raz)
    LaunchedEffect(quoteId) {
        if (!initialized && quoteDao != null && quoteId != null) {
            val quote = quoteDao.getQuoteById(quoteId).first().firstOrNull()
            if (quote != null) {
                text = quote.text ?: ""
                author = quote.author ?: ""
                source = quote.source ?: ""
                photoUri = quote.photoUri ?: Uri.EMPTY
                audioUri = quote.audioUri ?: Uri.EMPTY
                sourceType = quote.sourceType ?: SourceType.OTHER
                initialized = true
            }
        }
    }

    // Launchery do zdjęcia/audio
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopBar(
                title = "Edytuj cytat",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(8.dp)
                .fillMaxSize()
        ) {
            // Treść cytatu
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Treść") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Autor
            TextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Autor") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Źródło
            TextField(
                value = source,
                onValueChange = { source = it },
                label = { Text("Źródło") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Rodzaj źródła
            SourceTypeDropdown(
                selected = sourceType,
                onSelected = { sourceType = it }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Zdjęcie z przyciskiem usuwania
            Box(modifier = Modifier.fillMaxWidth()) {
                if (photoUri != Uri.EMPTY) {
                    ImageFromUri(photoUri)
                    IconButton(
                        onClick = { photoUri = Uri.EMPTY },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Usuń zdjęcie"
                        )
                    }
                }
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = {
                        imagePickerLauncher.launch(arrayOf("image/*"))
                    }
                ) {
                    Text("Wybierz zdjęcie")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Audio z przyciskiem usuwania
            Box(modifier = Modifier.fillMaxWidth()) {
                if (audioUri != Uri.EMPTY) {
                    AudioControlButton(audioUri)
                    IconButton(
                        onClick = { audioUri = Uri.EMPTY },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Usuń audio"
                        )
                    }
                }
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = {
                        audioPickerLauncher.launch(arrayOf("audio/*"))
                    }
                ) {
                    Text("Wybierz audio")
                }
            }
            // Wyświetl skrót tytułu (nazwę pliku audio)
            if (audioUri != Uri.EMPTY) {
                val fileName = remember(audioUri) {
                    var result: String? = null
                    val cursor = context.contentResolver.query(audioUri, null, null, null, null)
                    cursor?.use {
                        if (it.moveToFirst()) {
                            result = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        }
                    }
                    result ?: audioUri.lastPathSegment
                }
                Text(
                    text = fileName ?: "Wybrano plik audio",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Przycisk Anuluj i Zapisz
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFBDBD))
                ) {
                    Text("Anuluj")
                }
                Button(
                    onClick = {
                        if (quoteDao != null && quoteId != null && text.isNotBlank()) {
                            scope.launch {
                                val updatedQuote = Quote(
                                    id = quoteId,
                                    text = text,
                                    author = author,
                                    source = source,
                                    photoUri = photoUri,
                                    audioUri = audioUri,
                                    sourceType = sourceType
                                )
                                quoteDao.updateQuote(updatedQuote)
                                navController.popBackStack()
                            }
                        }
                    },
                    enabled = text.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDFCC9))
                ) {
                    Text("Zapisz")
                }
            }
        }
    }
}
