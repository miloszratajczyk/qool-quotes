package com.example.qoolquotes.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.navigation.TutorialScreenDestination

@Composable
fun HelpButton() {
    val navController = LocalNavController.current

    TextButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { navController.navigate(TutorialScreenDestination) },
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text("Jak dodawać cytaty?")
    }
}

