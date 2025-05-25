package com.example.qoolquotes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qoolquotes.data.QuoteDao
import com.example.qoolquotes.navigation.BrowseScreenDestination
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.navigation.bottomNavItems
import com.example.qoolquotes.ui.components.MyBottomBar
import com.example.qoolquotes.ui.components.MyTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(selectedView: String, modifier: Modifier = Modifier, quoteDao: QuoteDao) {
    val bottomNavController = rememberNavController()

    val startDestination = bottomNavItems.firstOrNull { it.route == selectedView }?.route ?: "images"
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopBar(
                title = "Lista Cytatów",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack
            )
        },
        bottomBar = {
            MyBottomBar(navController = bottomNavController, items = bottomNavItems)
        }

    ) { innerPadding ->

        NavHost(
            navController = bottomNavController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("images") { BrowseImagesScreen(quoteDao = quoteDao) }
            composable("sounds") { BrowseSoundsScreen(quoteDao = quoteDao) }
            composable("texts") { BrowseTextsScreen(quoteDao = quoteDao) }
        }
    }
}
