package com.example.qoolquotes.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.qoolquotes.data.Quote


@Composable
fun SwipeableCardDeck(cards: List<Quote>) {
    var currentIndex by remember { mutableStateOf(0) }
    var triggerAutoSwipe by remember { mutableStateOf(false) }

    if (currentIndex >= cards.size) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Koniec cytatów :c")
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
                    triggerAutoSwipe = isTopCard && triggerAutoSwipe,
                    onSwiped = {
                        currentIndex += 1
                        triggerAutoSwipe = false
                    },
                    modifier = Modifier
                        .offset(y = (index * 8).dp)
                        .zIndex(index.toFloat())
                )
            }
        }

        Button(
            onClick = { triggerAutoSwipe = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Text("Następny cytat")
        }
    }
}