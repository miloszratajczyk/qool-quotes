package com.example.qoolquotes.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.ui.components.HelpButton
import com.example.qoolquotes.ui.components.MyTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onChangeTheme: () -> Unit = {},
    onFontChange: (String) -> Unit = {},
    onDeleteAllQuotes: () -> Unit = {}
) {
    val navController = LocalNavController.current
    var showFontDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val fonts = listOf("Default", "Serif", "Monospace", "Cursive")
    var selectedFont by remember { mutableStateOf("Default") } // Dodaj, jeśli wyświetlasz wybraną czcionkę

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopBar(
                title = "Ustawienia",
                hideSettingsButton = true,
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            SettingsItem(
                icon = Icons.Default.DarkMode,
                text = "Zmień motyw",
                onClick = onChangeTheme
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SettingsItem(
                icon = Icons.Default.TextFields,
                text = "Zmień czcionkę",
                onClick = { showFontDialog = true }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SettingsItem(
                icon = Icons.Default.Delete,
                text = "Usuń wszystkie dane",
                iconTint = MaterialTheme.colorScheme.error,
                onClick = { showDeleteDialog = true }
            )

            Spacer(Modifier.height(16.dp))
            Text("Wybrana czcionka: $selectedFont", fontSize = 16.sp)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Spacer(Modifier.height(16.dp))
            HelpButton()
        }
    }

    // Dialog potwierdzający usunięcie wszystkich cytatów
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Potwierdź usunięcie") },
            text = { Text("Wszystkie cytaty zostaną usunięte. Czy chcesz kontynuować?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteAllQuotes()
                    }
                ) { Text("Tak, usuń") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Anuluj") }
            }
        )
    }

    // (Dialog wyboru czcionki – opcjonalnie możesz tu zostawić)
    if (showFontDialog) {
        AlertDialog(
            onDismissRequest = { showFontDialog = false },
            title = { Text("Wybierz czcionkę") },
            text = {
                Column {
                    fonts.forEach { font ->
                        Text(
                            text = font,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedFont = font
                                    showFontDialog = false
                                    onFontChange(font)
                                }
                                .padding(8.dp)
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    text: String,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
