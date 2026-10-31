// Integra YouTube IFrame Player con el ciclo de vida de la pantalla.
package com.example.proyecto_semestral.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun EmbeddedVideoPlayer(videoUrl: String, modifier: Modifier = Modifier) {
    if (LocalInspectionMode.current) {
        Text("Reproductor YouTube", modifier.height(230.dp))
        return
    }
    val videoId = remember(videoUrl) { youtubeVideoId(videoUrl) }
    if (videoId == null) {
        Text("Ingresa un enlace válido de YouTube.", color = MaterialTheme.colorScheme.error)
        return
    }
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var error by remember(videoId) { mutableStateOf<String?>(null) }
    val playerView = remember(context, videoId) { YouTubePlayerView(context) }
    DisposableEffect(playerView, lifecycle) {
        lifecycle.addObserver(playerView)
        val listener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    youTubePlayer.loadVideo(videoId, 0f)
                } else {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            }
            override fun onError(youTubePlayer: YouTubePlayer, errorCode: PlayerConstants.PlayerError) {
                error = "No se pudo reproducir el video: $errorCode"
            }
        }
        playerView.addYouTubePlayerListener(listener)
        onDispose {
            lifecycle.removeObserver(playerView)
            playerView.release()
        }
    }
    Column(modifier) {
        key(playerView) {
            AndroidView(factory = { playerView }, modifier = Modifier.fillMaxWidth().height(230.dp))
        }
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}

private fun youtubeVideoId(url: String): String? = runCatching {
    val uri = Uri.parse(url.trim())
    val host = uri.host.orEmpty().lowercase().removePrefix("www.").removePrefix("m.")
    val id = when (host) {
        "youtu.be" -> uri.pathSegments.firstOrNull()
        "youtube.com", "youtube-nocookie.com" -> uri.getQueryParameter("v")
            ?: uri.pathSegments.takeIf { it.firstOrNull() in listOf("embed", "shorts", "live") }?.getOrNull(1)
        else -> null
    }
    id?.takeIf { it.matches(Regex("[A-Za-z0-9_-]{11}")) }
}.getOrNull()
