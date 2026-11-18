package com.example.proyecto_semestral.ui.components

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import java.io.ByteArrayOutputStream

/**
 * Decodifica una imagen de la galería, la reduce al lado máximo indicado y la
 * comprime como JPEG. Sirve tanto para imágenes de administración como de perfil.
 */
fun compressImage(
    resolver: ContentResolver,
    uri: Uri,
    maxSide: Int = 1600,
    jpegQuality: Int = 85
): ByteArray {
    require(maxSide > 0) { "El tamaño máximo debe ser mayor que cero" }
    val decoded = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        decodeModernImage(resolver, uri, maxSide)
    } else {
        decodeLegacyImage(resolver, uri, maxSide)
    }

    val scaled = decoded.scaleDown(maxSide)
    return try {
        ByteArrayOutputStream().use { output ->
            check(scaled.compress(Bitmap.CompressFormat.JPEG, jpegQuality, output)) {
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
    uri: Uri,
    maxSide: Int
): Bitmap {
    val source = ImageDecoder.createSource(resolver, uri)
    return ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        decoder.isMutableRequired = false

        val width = info.size.width
        val height = info.size.height
        val largest = maxOf(width, height)
        if (largest > maxSide) {
            val ratio = maxSide.toFloat() / largest
            decoder.setTargetSize(
                (width * ratio).toInt().coerceAtLeast(1),
                (height * ratio).toInt().coerceAtLeast(1)
            )
        }
    }
}

private fun decodeLegacyImage(
    resolver: ContentResolver,
    uri: Uri,
    maxSide: Int
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
        bounds.outWidth / sampleSize > maxSide ||
        bounds.outHeight / sampleSize > maxSide
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

private fun Bitmap.scaleDown(maxSide: Int): Bitmap {
    val largest = maxOf(width, height)
    if (largest <= maxSide) return this

    val ratio = maxSide.toFloat() / largest
    return Bitmap.createScaledBitmap(
        this,
        (width * ratio).toInt().coerceAtLeast(1),
        (height * ratio).toInt().coerceAtLeast(1),
        true
    )
}
