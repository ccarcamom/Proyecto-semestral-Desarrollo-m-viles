package com.example.proyecto_semestral.ui.screens.admin

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import java.io.ByteArrayOutputStream

private const val MAX_IMAGE_SIDE = 1600
private const val JPEG_QUALITY = 85

internal fun compressAdminImage(
    resolver: ContentResolver,
    uri: Uri
): ByteArray {
    val decoded = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        decodeModernImage(resolver, uri)
    } else {
        decodeLegacyImage(resolver, uri)
    }

    val scaled = decoded.scaleDown()
    return try {
        ByteArrayOutputStream().use { output ->
            check(scaled.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, output)) {
                "No se pudo comprimir la imagen"
            }
            output.toByteArray()
        }
    } finally {
        if (scaled !== decoded) scaled.recycle()
        decoded.recycle()
    }
}

@androidx.annotation.RequiresApi(Build.VERSION_CODES.P)
private fun decodeModernImage(
    resolver: ContentResolver,
    uri: Uri
): Bitmap {
    val source = ImageDecoder.createSource(resolver, uri)
    return ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        decoder.isMutableRequired = false

        val width = info.size.width
        val height = info.size.height
        val maxSide = maxOf(width, height)
        if (maxSide > MAX_IMAGE_SIDE) {
            val ratio = MAX_IMAGE_SIDE.toFloat() / maxSide
            decoder.setTargetSize(
                (width * ratio).toInt().coerceAtLeast(1),
                (height * ratio).toInt().coerceAtLeast(1)
            )
        }
    }
}

private fun decodeLegacyImage(
    resolver: ContentResolver,
    uri: Uri
): Bitmap {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    resolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it, null, bounds)
    } ?: error("No se pudo abrir la imagen")

    require(bounds.outWidth > 0 && bounds.outHeight > 0) {
        "El formato de la imagen no es compatible"
    }

    var sampleSize = 1
    while (
        bounds.outWidth / sampleSize > MAX_IMAGE_SIDE ||
        bounds.outHeight / sampleSize > MAX_IMAGE_SIDE
    ) {
        sampleSize *= 2
    }

    val options = BitmapFactory.Options().apply {
        inSampleSize = sampleSize
        inPreferredConfig = Bitmap.Config.ARGB_8888
    }
    return resolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it, null, options)
    } ?: error("No se pudo decodificar la imagen")
}

private fun Bitmap.scaleDown(): Bitmap {
    val maxSide = maxOf(width, height)
    if (maxSide <= MAX_IMAGE_SIDE) return this

    val ratio = MAX_IMAGE_SIDE.toFloat() / maxSide
    return Bitmap.createScaledBitmap(
        this,
        (width * ratio).toInt().coerceAtLeast(1),
        (height * ratio).toInt().coerceAtLeast(1),
        true
    )
}
