package com.example.qoolquotes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.ui.components.HelpButton
import com.example.qoolquotes.ui.components.MyTopBar
import com.example.qoolquotes.ui.components.SwipeableCardDeck
import com.example.qoolquotes.viewmodel.SlideshowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlideshowScreen(viewModel: SlideshowViewModel = hiltViewModel()) {
    LocalNavController.current
    val quotes by viewModel.imageQuotes.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            MyTopBar(title = "Pokaz slajdów", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack)
        }

    ) { innerPadding ->
        if (quotes.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    "Nie znaleziono żadnych cytatów z obrazkiem.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                HelpButton()
            }
        } else {
            Column {

                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                ) {
                    SwipeableCardDeck(cards = quotes)
                }
            }
        }

    }
}


