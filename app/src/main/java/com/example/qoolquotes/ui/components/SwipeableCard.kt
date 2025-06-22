package com.example.qoolquotes.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.qoolquotes.data.Quote
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs


@Composable
fun SwipeableCard(
    quote: Quote,
    isDraggable: Boolean,
    onSwiped: () -> Unit,
    modifier: Modifier = Modifier,
    triggerAutoSwipe: Boolean = false
) {
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }

    val threshold = 300f
    val scope = rememberCoroutineScope()

    LaunchedEffect(triggerAutoSwipe) {
        if (triggerAutoSwipe) {
            val totalHops = 4
            val jumpDistance = 250f
            val jumpHeight = 200f
            val stepsPerHop = 10
            val delayPerStep = 16L

            repeat(totalHops) { hop ->
                val startX = offsetX.value
                val endX = startX + jumpDistance

                for (step in 0..stepsPerHop) {
                    val t = step / stepsPerHop.toFloat()
                    val x = lerp(startX, endX, t)
                    val y = -4 * jumpHeight * t * (1 - t) // parabola

                    offsetX.snapTo(x)
                    offsetY.snapTo(y)
                    delay(delayPerStep)
                }
            }

            offsetX.animateTo(
                offsetX.value + 1000f,
                animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
            )
            rotation.animateTo(25f)
            delay(300)
            onSwiped()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth(0.85f)
            .aspectRatio(0.7f)
            .graphicsLayer(
                translationX = offsetX.value,
                translationY = offsetY.value,
                rotationZ = rotation.value
            )
            .pointerInput(isDraggable) {
                if (!isDraggable) return@pointerInput

                detectDragGestures(onDrag = { change, dragAmount ->
                    change.consume()
                    scope.launch {
                        offsetX.snapTo(offsetX.value + dragAmount.x)
                        offsetY.snapTo(offsetY.value + dragAmount.y)
                        rotation.snapTo((offsetX.value / 40).coerceIn(-40f, 40f))
                    }
                }, onDragEnd = {
                    if (abs(offsetX.value) > threshold) {
                        val targetX = if (offsetX.value > 0) 1000f else -1000f
                        scope.launch {
                            launch { offsetX.animateTo(targetX) }
                            launch { rotation.animateTo(rotation.value * 2) }
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
                })
            }
            .padding(8.dp)) {
        QuoteCard(quote = quote)
    }
}