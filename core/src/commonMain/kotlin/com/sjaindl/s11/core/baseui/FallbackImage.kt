package com.sjaindl.s11.core.baseui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import startingeleven.core.generated.resources.Res
import startingeleven.core.generated.resources.ic_user

@Composable
fun FallbackImage(
    photoRefDownloadUrl: String? = null,
    photoUrl: String? = null,
    fallback: DrawableResource = Res.drawable.ic_user,
    cacheEnabled: Boolean = true,
    additionalCacheKey: String? = null,
) {
    Column {
        if (!photoRefDownloadUrl.isNullOrEmpty()) {
            AsyncImage(url = photoRefDownloadUrl, cacheEnabled = cacheEnabled, additionalCacheKey = additionalCacheKey)
        } else if (!photoUrl.isNullOrEmpty()) {
            AsyncImage(url = photoUrl, cacheEnabled = cacheEnabled, additionalCacheKey = additionalCacheKey)
        } else {
            Image(
                painter = painterResource(fallback),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp),
            )
        }
    }
}

@Composable
private fun AsyncImage(url: String, cacheEnabled: Boolean, additionalCacheKey: String?) {
    val context = LocalPlatformContext.current

    val cachePolicy = if (cacheEnabled) {
        CachePolicy.ENABLED
    } else {
        CachePolicy.DISABLED
    }

    val cacheKey = url + additionalCacheKey.orEmpty()

    val imageRequest = ImageRequest.Builder(context = context)
        .data(data = url)
        .dispatcher(dispatcher = Dispatchers.Default)
        .memoryCacheKey(key = cacheKey)
        .diskCacheKey(key = cacheKey)
        .diskCachePolicy(policy = cachePolicy)
        .memoryCachePolicy(policy = cachePolicy)
        .build()

    SubcomposeAsyncImage(
        modifier = Modifier
            .size(size = 120.dp),
        model = imageRequest,
        loading = {
            LoadingScreen(
                modifier = Modifier
                    .size(size = 120.dp)
                    .border(width = 1.dp, color = colorScheme.onBackground)
                    .padding(all = 16.dp),
            )
        },
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}
