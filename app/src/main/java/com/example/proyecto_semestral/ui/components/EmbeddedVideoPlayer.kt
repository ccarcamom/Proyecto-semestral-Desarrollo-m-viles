// Reproduce enlaces de YouTube o archivos de video dentro de la aplicación.
package com.example.proyecto_semestral.ui.components

import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun EmbeddedVideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    if (LocalInspectionMode.current) {
        VideoPreviewPlaceholder(modifier)
        return
    }

    val html = remember(videoUrl) { videoHtml(videoUrl) }
    var webView by remember { mutableStateOf<WebView?>(null) }

    AndroidView(
        factory = { context ->
            WebView(context).apply player@ {
                setBackgroundColor(android.graphics.Color.BLACK)
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mediaPlaybackRequiresUserGesture = true
                settings.loadsImagesAutomatically = true
                settings.allowContentAccess = true
                CookieManager.getInstance().apply {
                    setAcceptCookie(true)
                    setAcceptThirdPartyCookies(this@player, true)
                }
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                webView = this
            }
        },
        update = { view ->
            if (view.tag != videoUrl) {
                view.tag = videoUrl
                view.loadDataWithBaseURL(
                    PLAYER_ORIGIN,
                    html,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(18.dp))
    )

    DisposableEffect(Unit) {
        onDispose {
            webView?.stopLoading()
            webView?.destroy()
            webView = null
        }
    }
}

@Composable
private fun VideoPreviewPlaceholder(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text("Reproductor de video", color = Color.White)
    }
}

private fun videoHtml(videoUrl: String): String {
    val youtubeId = youtubeVideoId(videoUrl)
    return if (youtubeId != null) {
        """
        <!doctype html>
        <html><head>
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <meta name="referrer" content="strict-origin-when-cross-origin">
          <style>html,body,iframe{margin:0;width:100%;height:100%;background:#000;border:0;overflow:hidden}</style>
        </head><body>
          <iframe src="https://www.youtube.com/embed/$youtubeId?playsinline=1&origin=https%3A%2F%2Fmastermartini.cl"
            allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
            referrerpolicy="strict-origin-when-cross-origin"
            allowfullscreen></iframe>
        </body></html>
        """.trimIndent()
    } else {
        val safeUrl = videoUrl
            .replace("&", "&amp;")
            .replace("\"", "&quot;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        """
        <!doctype html>
        <html><head>
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <style>html,body,video{margin:0;width:100%;height:100%;background:#000}</style>
        </head><body>
          <video controls playsinline src="$safeUrl"></video>
        </body></html>
        """.trimIndent()
    }
}

private const val PLAYER_ORIGIN = "https://mastermartini.cl/"

private fun youtubeVideoId(url: String): String? {
    return runCatching {
        val uri = Uri.parse(url.trim())
        val host = uri.host.orEmpty().lowercase().removePrefix("www.").removePrefix("m.")
        val candidate = when {
            host == "youtu.be" -> uri.pathSegments.firstOrNull()
            host == "youtube.com" -> uri.getQueryParameter("v")
                ?: uri.pathSegments.dropWhile { it != "embed" && it != "shorts" }.getOrNull(1)
            else -> null
        }
        candidate?.takeIf { it.matches(Regex("[A-Za-z0-9_-]{6,}")) }
    }.getOrNull()
}

@Preview(showBackground = true, widthDp = 400)
@Composable
private fun EmbeddedVideoPlayerPreview() {
    ProyectoSemestralTheme {
        EmbeddedVideoPlayer(videoUrl = "https://youtube.com/watch?v=ejemplo")
    }
}
