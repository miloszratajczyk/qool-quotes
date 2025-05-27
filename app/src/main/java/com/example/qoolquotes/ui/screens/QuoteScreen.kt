import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.qoolquotes.data.Quote
import com.example.qoolquotes.data.QuoteDao
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.ui.components.MyTopBar
import com.example.qoolquotes.ui.screens.BrowseTextsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen(
    quoteId: Long? = null,
    quoteDao: QuoteDao
) {
    val navController = LocalNavController.current
    var quote by remember { mutableStateOf<Quote?>(null) }

    // Pobierz cytat na podstawie ID
    LaunchedEffect(quoteId) {
        if (quoteId != null) {
            quoteDao.getQuoteById(quoteId).collect { quotes ->
                if (quotes.isNotEmpty()) {
                    quote = quotes[0]
                }
            }
        }
    }

    Scaffold(
        topBar = {
            MyTopBar(
                title = "Podgląd cytatu",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack
            )
        }
    ) { innerPadding ->
        quote?.let { q ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Obraz cytatu
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(top = 16.dp)
                ) {
                    q.photoUri?.let { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = "Tło cytatu",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = 16.dp,
                                        bottomEnd = 16.dp
                                    )
                                )
                        )
                    }
                }

                // Treść cytatu
                Text(
                    text = q.text,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Źródło i typ źródła
                Text(
                    text = "${q.author}, ${q.source}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = q.sourceType.label,
                    style = MaterialTheme.typography.bodySmall
                )

                // Dolne przyciski: usuń - odtwórz - edytuj
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Usuń
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                              // usuwanie todo
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Usuń cytat",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }

                    // Odtwórz (placeholder)
                    IconButton(onClick = {
                        // Można dodać Text-to-Speech lub inny efekt
                    }) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Odtwórz cytat"
                        )
                    }

                    // Edytuj
                    IconButton(onClick = {
                        // Nawigacja do ekranu edycji (jeśli istnieje)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edytuj cytat"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}