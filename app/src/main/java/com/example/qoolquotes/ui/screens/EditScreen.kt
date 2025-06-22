package com.example.qoolquotes.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.qoolquotes.data.Quote
import com.example.qoolquotes.data.QuoteDao
import com.example.qoolquotes.data.SourceType
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.ui.components.AudioControlButton
import com.example.qoolquotes.ui.components.ImageFromUri
import com.example.qoolquotes.ui.components.MyTopBar
import com.example.qoolquotes.ui.components.SourceTypeDropdown
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    quoteId: Int? = null, modifier: Modifier = Modifier, quoteDao: QuoteDao? = null
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(false) }

    var text by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var source by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    var audioUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    var sourceType by remember { mutableStateOf(SourceType.OTHER) }
    var initialized by remember { mutableStateOf(false) }

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

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            photoUri = uri
        }
    }
    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            audioUri = uri
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        MyTopBar(
            title = "Edytuj cytat", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack
        )
    }, snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(8.dp)
                .fillMaxSize()
        ) {
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
                label = { Text("Autor") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = source,
                onValueChange = { source = it },
                label = { Text("Źródło") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            SourceTypeDropdown(
                selected = sourceType, onSelected = { sourceType = it })
            Spacer(modifier = Modifier.height(8.dp))

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
                            imageVector = Icons.Default.Delete, contentDescription = "Usuń zdjęcie"
                        )
                    }
                }
                Button(
                    modifier = Modifier.align(Alignment.Center), onClick = {
                        imagePickerLauncher.launch(arrayOf("image/*"))
                    }) {
                    Text("Wybierz zdjęcie")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

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
                            imageVector = Icons.Default.Delete, contentDescription = "Usuń audio"
                        )
                    }
                }
                Button(
                    modifier = Modifier.align(Alignment.Center), onClick = {
                        audioPickerLauncher.launch(arrayOf("audio/*"))
                    }) {
                    Text("Wybierz audio")
                }
            }
            if (audioUri != Uri.EMPTY) {
                val fileName = remember(audioUri) {
                    var result: String? = null
                    val cursor = context.contentResolver.query(audioUri, null, null, null, null)
                    cursor?.use {
                        if (it.moveToFirst()) {
                            val columnId = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            if (columnId >= 0) {

                                result = it.getString(columnId)
                            }
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

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = contentColorFor(backgroundColor = Color.Red)
                    )
                ) {
                    Text("Anuluj")
                }
                Button(
                    onClick = {
                        if (quoteDao != null && quoteId != null) {
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
                                // Ustawiamy flagę do wyświetlenia snackbara po powrocie
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                        "quote_edited",
                                        true
                                    )
                                navController.popBackStack()
                            }
                        }
                    },
                    enabled = text.trim().isNotEmpty() && author.trim()
                        .isNotEmpty() && source.trim().isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green,
                        contentColor = contentColorFor(backgroundColor = Color.Green)
                    )
                ) {
                    Text("Zapisz")
                }
            }

            if (showSnackbar) {
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar("Zapisano zmiany")
                    showSnackbar = false
                }
            }
        }
    }
}
