package com.example.proyecto_semestral.ui.screens.admin

import android.content.ContentResolver
import android.net.Uri
import com.example.proyecto_semestral.ui.components.compressImage

internal fun compressAdminImage(
    resolver: ContentResolver,
    uri: Uri
): ByteArray = compressImage(resolver = resolver, uri = uri)
