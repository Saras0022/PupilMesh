package com.saras.pupilmesh.utils

import android.content.Context
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import kotlinx.coroutines.Dispatchers

fun imageRequest(context: Context, imageUrl: String): ImageRequest {
    return ImageRequest.Builder(context)
        .data(imageUrl)
        .coroutineContext(Dispatchers.IO)
        .memoryCacheKey(imageUrl)
        .diskCacheKey(imageUrl)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .build()
}