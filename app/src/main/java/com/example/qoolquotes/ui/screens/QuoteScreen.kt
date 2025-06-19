package com.example.qoolquotes.ui.screens

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import com.example.qoolquotes.data.Quote
import com.example.qoolquotes.data.QuoteDao
import com.example.qoolquotes.navigation.EditScreenDestination
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.ui.components.MyTopBar
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen(
    quoteId: Int? = null,
    quoteDao: QuoteDao
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    var quote by remember { mutableStateOf<Quote?>(null) }
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    // TTS obsługa
    val tts = remember {
        var ttsEngine: TextToSpeech? = null
        ttsEngine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsEngine?.language = Locale.getDefault()
            }
        }
        ttsEngine
    }
    DisposableEffect(Unit) {
        onDispose { tts.shutdown() }
    }

    // Pobierz cytat na podstawie ID (teraz Int!)
    LaunchedEffect(quoteId) {
        if (quoteId != null) {
            quoteDao.getQuoteById(quoteId).collect { quotes: List<Quote> ->
                if (quotes.isNotEmpty()) {
                    quote = quotes[0]
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                MyTopBar(
                    title = "Podgląd cytatu",
                    navigationIcon = Icons.AutoMirrored.Filled.ArrowBack
                )
            }
        ) { innerPadding ->

            // Potwierdzenie usuwania
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Usunąć cytat?") },
                    text = { Text("Tej operacji nie można cofnąć.") },
                    confirmButton = {
                        TextButton(onClick = {
                            scope.launch {
                                quote?.let { q ->
                                    quoteDao.delete(q)
                                    showDialog = false
                                    navController.popBackStack()
                                }
                            }
                        }) {
                            Text("Tak")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Nie")
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.96f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 12.dp, horizontal = 6.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Większy obraz cytatu
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.3f)
                                .clip(RoundedCornerShape(18.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            quote?.photoUri?.let { uri ->
                                AsyncImage(
                                    model = uri,
                                    contentDescription = "Tło cytatu",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(18.dp))
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Treść cytatu
                        Text(
                            text = quote?.text ?: "",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 18.sp,
                                lineHeight = 24.sp
                            ),
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // DODATKOWE DANE: AUTOR, ŹRÓDŁO, TYP ŹRÓDŁA
                        quote?.let { q ->
                            if (!q.author.isNullOrBlank() || !q.source.isNullOrBlank()) {
                                Text(
                                    text = listOfNotNull(q.author, q.source)
                                        .filter { it.isNotBlank() }
                                        .joinToString(", "),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .padding(top = 1.dp)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                            if (q.sourceType?.label != null) {
                                Text(
                                    text = q.sourceType.label,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .padding(top = 0.dp)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Przyciski
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Usuń (z popupem)
                            IconButton(
                                onClick = { showDialog = true },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Usuń cytat",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            // Play (Text-to-Speech)
                            IconButton(
                                onClick = {
                                    quote?.text?.let { tts.speak(it, TextToSpeech.QUEUE_FLUSH, null, null) }
                                },
                                modifier = Modifier
                                    .size(64.dp)
                                    .border(
                                        2.dp,
                                        MaterialTheme.colorScheme.onSurface,
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Odtwórz cytat",
                                    modifier = Modifier.size(36.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            // Edytuj (nawigacja do EditScreen)
                            IconButton(
                                onClick = {
                                    quote?.id?.let { navController.navigate(EditScreenDestination(it)) }
                                },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edytuj cytat",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
