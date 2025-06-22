package com.example.qoolquotes.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.qoolquotes.data.Quote
import com.example.qoolquotes.navigation.BrowseScreenDestination
import com.example.qoolquotes.navigation.LocalNavController
import com.example.qoolquotes.navigation.QuoteScreenDestination
import com.example.qoolquotes.ui.components.MyBottomBar
import com.example.qoolquotes.ui.components.MyTopBar
import com.example.qoolquotes.ui.components.QuoteCard
import com.example.qoolquotes.viewmodel.BrowseImagesViewModel
import com.example.qoolquotes.viewmodel.SlideshowViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import androidx.compose.runtime.key

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlideshowScreen( viewModel: SlideshowViewModel = hiltViewModel()) {
    val navController = LocalNavController.current
    val quotes by viewModel.imageQuotes.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MyTopBar(title = "Slideshow",navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,)
        }

    ) { innerPadding ->
        if (quotes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Nie znaleziono żadnych cytatów z obrazkiem.", fontSize = 18.sp)
            }
        } else {
            Box(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
            ) {
                SwipeableCardDeck( cards = quotes)
            }
        }

    }
}


@Composable
fun SwipeableCardDeck(cards: List<Quote>) {
    var currentIndex by remember { mutableStateOf(0) }

    if (currentIndex >= cards.size) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Koniec cytatów 🎉")
        }
        return
    }

    val visibleCards = cards.drop(currentIndex).take(3)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        visibleCards.reversed().forEachIndexed { index, quote ->
            val isTopCard = index == visibleCards.lastIndex

            key(quote.id) {
                SwipeableCard(
                    quote = quote,
                    isDraggable = isTopCard,
                    onSwiped = {
                        currentIndex += 1
                    },
                    modifier = Modifier
                        .offset(y = (index * 8).dp)
                        .zIndex(index.toFloat())
                )
            }
        }
    }
}

@Composable
fun SwipeableCard(
    quote: Quote,
    isDraggable: Boolean,
    onSwiped: () -> Unit,
    modifier: Modifier = Modifier
) {
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }

    val threshold = 300f

    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxWidth(0.8f)
            .aspectRatio(0.7f)
            .graphicsLayer(
                translationX = offsetX.value,
                translationY = offsetY.value,
                rotationZ = rotation.value
            )
//            .background(Color.White, shape = RoundedCornerShape(16.dp))
//            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .pointerInput(isDraggable) {
                if (!isDraggable) return@pointerInput
                detectDragGestures(
                    onDragEnd = {
                        if (abs(offsetX.value) > threshold) {
                            val targetX = if (offsetX.value > 0) 1000f else -1000f
                            scope.launch {
                                launch { offsetX.animateTo(targetX) }
                                launch { rotation.animateTo(rotation.value * 4) }
                                delay(300)
                                onSwiped()
                            }
                        } else {
                            scope.launch {
                                launch { offsetX.animateTo(0f) }
                                launch { offsetY.animateTo(0f) }
                                launch { rotation.animateTo(0f) }
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                            offsetY.snapTo(offsetY.value + dragAmount.y)
                            rotation.snapTo((offsetX.value / 60).coerceIn(-40f, 40f))
                        }
                    }
                )
            }
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            QuoteCard(quote)
        }
    }
}
