package com.example.qoolquotes.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AutoResizingText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    maxLines: Int = 8
) {
    BoxWithConstraints(modifier = modifier) {
        var textStyle by remember {
            mutableStateOf(TextStyle(fontSize = 24.sp))
        }
        var readyToDraw by remember { mutableStateOf(false) }

        Text(
            text = text,
            color = color,
            style = textStyle,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            softWrap = true,
            onTextLayout = { result ->
                if (!readyToDraw && result.hasVisualOverflow) {
                    textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.9)
                } else {
                    readyToDraw = true
                }
            }
        )
    }
}