package com.example.proyecto_semestral.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_semestral.R
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun VideoPlayerPlaceholder(
    videoId: String,
    @DrawableRes imageRes: Int,
    duration: String,
    modifier: Modifier = Modifier
) {
    var isPlaying by rememberSaveable(videoId) { mutableStateOf(false) }
    var captionsEnabled by rememberSaveable(videoId) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = "Portada del video",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { isPlaying = !isPlaying },
                contentAlignment = Alignment.Center
            ) {
                PlaybackIcon(
                    isPlaying = isPlaying,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(38.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.55f))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "0:00 / $duration",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "DEMO",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
            )
            Spacer(modifier = Modifier.width(28.dp))
            Text(
                text = "CC",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (captionsEnabled) {
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
                        } else {
                            Color.Transparent
                        }
                    )
                    .clickable { captionsEnabled = !captionsEnabled }
                    .padding(horizontal = 18.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun PlaybackIcon(
    isPlaying: Boolean,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val stroke = 2.5.dp.toPx()
        if (isPlaying) {
            drawLine(color, Offset(size.width * .32f, size.height * .22f), Offset(size.width * .32f, size.height * .78f), stroke, cap = StrokeCap.Round)
            drawLine(color, Offset(size.width * .68f, size.height * .22f), Offset(size.width * .68f, size.height * .78f), stroke, cap = StrokeCap.Round)
        } else {
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(size.width * .28f, size.height * .18f)
                lineTo(size.width * .76f, size.height * .5f)
                lineTo(size.width * .28f, size.height * .82f)
                close()
            }
            drawPath(path, color, style = Stroke(stroke))
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
private fun VideoPlayerPlaceholderPreview() {
    ProyectoSemestralTheme {
        VideoPlayerPlaceholder(
            videoId = "video-preview",
            imageRes = R.drawable.chocolate,
            duration = "8:00",
            modifier = Modifier.padding(24.dp)
        )
    }
}
