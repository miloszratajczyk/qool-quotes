package com.example.qoolquotes.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.ui.graphics.vector.ImageVector


data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("Obrazy", "images", Icons.Default.Image),
    BottomNavItem("Dźwięki", "sounds", Icons.Default.MusicNote),
    BottomNavItem("Teksty", "texts", Icons.Default.List)
)