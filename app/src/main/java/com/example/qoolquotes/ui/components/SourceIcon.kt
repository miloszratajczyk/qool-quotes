package com.example.qoolquotes.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.qoolquotes.data.SourceType

@Composable
fun sourceIcon(sourceType: SourceType): ImageVector = when (sourceType) {
    SourceType.BOOK -> Icons.AutoMirrored.Filled.MenuBook
    SourceType.MOVIE -> Icons.Default.Movie
    SourceType.SONG -> Icons.Default.MusicNote
    SourceType.OTHER -> Icons.Default.Info
}