package com.example.qoolquotes.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector


data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("Obrazy", "images", Icons.Default.Home),
    BottomNavItem("Dźwięki", "sounds", Icons.Default.Call),
    BottomNavItem("Teksty", "texts", Icons.Default.Build)
)