package com.example.proyecto_semestral.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto_semestral.R
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

@Composable
fun BrandLogo(
    @DrawableRes logoResId: Int = R.drawable.logo,
    modifier: Modifier = Modifier,
    contentDescription: String = "Logo Master Martini"
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = logoResId),
            contentDescription = contentDescription,
            modifier = Modifier.sizeIn(
                minWidth = 80.dp,
                maxWidth = 140.dp,
                minHeight = 72.dp,
                maxHeight = 120.dp
            ),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "APRENDE - CREA - CRECE",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BrandLogoPreview() {
    ProyectoSemestralTheme {
        BrandLogo(
            modifier = Modifier.padding(24.dp)
        )
    }
}
