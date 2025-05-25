import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen(
    quoteId: Long? = null,
    quoteDao: QuoteDao
) {
    val navController = LocalNavController.current
    var quote by remember { mutableStateOf<Quote?>(null) }

    // Jeśli mamy quoteId, ładujemy szczegóły cytatu
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
                title = if (quoteId == null) "Wszystkie cytaty" else "Szczegóły cytatu",
                navigationIcon = if (quoteId != null) Icons.AutoMirrored.Filled.ArrowBack else null
            )
        },
        content = { innerPadding ->
            if (quoteId == null) {
                // Widok listy cytatów
                BrowseTextsScreen(quoteDao = quoteDao)
            } else {
                // Widok szczegółów cytatu
                quote?.let { q ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Wyświetl szczegóły cytatu
                        Row {
                            if (q.author.isNotBlank()) {
                                Text(
                                    text = "${q.author},",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                            if (q.source?.isNotBlank() == true) {
                                Text(
                                    text = "'${q.source}'",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = q.text,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                            modifier = Modifier.padding(vertical = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = q.sourceType.label,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    )
}