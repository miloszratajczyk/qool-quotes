package com.example.qoolquotes.ui.screens

import android.widget.VideoView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.example.qoolquotes.R
import com.example.qoolquotes.ui.components.MyTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialScreen() {
    val context = LocalContext.current
    val videoViewRef = remember { mutableStateOf<VideoView?>(null) }
    val isPlaying = remember { mutableStateOf(true) }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        MyTopBar(
            title = "Tutorial",
            hideSettingsButton = true,
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                val video = videoViewRef.value ?: return@FloatingActionButton
                if (video.isPlaying) {
                    video.pause()
                    isPlaying.value = false
                } else {
                    video.start()
                    isPlaying.value = true
                }
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = contentColorFor(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = if (isPlaying.value) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying.value) "Pauza" else "Odtwórz"
            )
        }
    }) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(bottom = 64.dp, top = 32.dp,end= 16.dp, start = 16.dp)
                .fillMaxSize()

        ) {
            Text("Poniższe nagranie przedstawia jak dodawać i edytować cytaty.")

            Spacer(Modifier.height(16.dp))
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(24.dp)
                    ), factory = {
                    VideoView(context).apply {
                        setVideoURI(
                            "android.resource://${context.packageName}/${R.raw.videotut}".toUri()
                        )
                        setOnPreparedListener {
                            it.isLooping = true
                            start()
                        }
                        videoViewRef.value = this
                    }
                })

        }
    }
}
