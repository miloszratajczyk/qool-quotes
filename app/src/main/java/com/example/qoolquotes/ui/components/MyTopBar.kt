package com.example.qoolquotes.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.navigation.SettingsScreenDestination

@ExperimentalMaterial3Api
@Composable
fun MyTopBar(
    title: String,
    hideBackButton: Boolean = false,
    hideSettingsButton: Boolean = false,
    navigationIcon: ImageVector?,
) {
    val navController = LocalNavController.current

    TopAppBar(
        title = {
            Text(text = title, fontSize = 20.sp)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White
        ),
        navigationIcon = {
            if (!hideBackButton) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            if (!hideSettingsButton) {
                IconButton(onClick = {navController.navigate(SettingsScreenDestination)}) {
                    Icon(Icons.Default.Settings, contentDescription = "Ustawienia", tint = Color.White)
                }
            }
        }
    )
}
