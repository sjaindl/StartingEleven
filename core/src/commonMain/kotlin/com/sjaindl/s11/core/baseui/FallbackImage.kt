package com.sjaindl.s11.core.baseui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.sjaindl.s11.core.theme.spacing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
    maxSize: Dp? = null,
) {
    var isFullscreen by remember {
        mutableStateOf(value = false)
    }

    var scale by remember {
        mutableStateOf(value = 1f)
    }

    var offsetX by remember {
        mutableStateOf(value = 0f)
    }

    var offsetY by remember {
        mutableStateOf(value = 0f)
    }

    if (isFullscreen) {
        Dialog(
            onDismissRequest = {
                isFullscreen = false
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false,
            )
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = spacing.xxl),
                contentAlignment = Alignment.TopEnd,
            ) {
                val imageModifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY,
                        transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0.5f),
                    )
                    .pointerInput(Unit) {
                        detectTransformGestures { centroid, pan, zoom, rotation ->
                            scale = (scale * zoom).coerceAtLeast(minimumValue = 1f) // Limit zoom to 1x minimum
                            offsetX += pan.x * scale
                            offsetY += pan.y * scale
                        }
                    }

                if (!photoRefDownloadUrl.isNullOrEmpty()) {
                    AsyncImage(
                        url = photoRefDownloadUrl,
                        cacheEnabled = cacheEnabled,
                        additionalCacheKey = additionalCacheKey,
                        modifier = imageModifier,
                        contentScale = ContentScale.Fit,
                    )
                } else if (!photoUrl.isNullOrEmpty()) {
                    AsyncImage(
                        url = photoUrl,
                        cacheEnabled = cacheEnabled,
                        additionalCacheKey = additionalCacheKey,
                        modifier = imageModifier,
                        contentScale = ContentScale.Fit,
                    )
                }

                IconButton(
                    onClick = {
                        isFullscreen = false
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            tint = colorScheme.primary,
                        )
                    }
                )

            }
        }
    }

    if (!photoRefDownloadUrl.isNullOrEmpty()) {
        AsyncImage(
            url = photoRefDownloadUrl,
            cacheEnabled = cacheEnabled,
            additionalCacheKey = additionalCacheKey,
            modifier = Modifier
                .clickable {
                    isFullscreen = true
                }.then(if (maxSize != null) {
                    Modifier.size(maxSize)
                } else {
                    Modifier
                }),
        )
    } else if (!photoUrl.isNullOrEmpty()) {
        AsyncImage(
            url = photoUrl,
            cacheEnabled = cacheEnabled,
            additionalCacheKey = additionalCacheKey,
            modifier = Modifier
                .clickable {
                    isFullscreen = true
                }.then(if (maxSize != null) {
                    Modifier.size(maxSize)
                } else {
                    Modifier
                }),
        )
    } else {
        Image(
            painter = painterResource(fallback),
            contentDescription = null,
            modifier = Modifier
                .size(size = 40.dp),
        )
    }
}

@Composable
private fun AsyncImage(
    url: String,
    cacheEnabled: Boolean,
    additionalCacheKey: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val context = LocalPlatformContext.current

    val cachePolicy = if (cacheEnabled) {
        CachePolicy.ENABLED
    } else {
        CachePolicy.DISABLED
    }

    val cacheKey = url + additionalCacheKey.orEmpty()

    val imageRequest = ImageRequest.Builder(context = context)
        .data(data = url)
        .coroutineContext(context = Dispatchers.IO)
        .memoryCacheKey(key = cacheKey)
        .diskCacheKey(key = cacheKey)
        .diskCachePolicy(policy = cachePolicy)
        .memoryCachePolicy(policy = cachePolicy)
        .build()

    SubcomposeAsyncImage(
        modifier = modifier,
        model = imageRequest,
        loading = {
            LoadingScreen(
                modifier = Modifier
                    .size(size = 120.dp)
                    .border(width = 1.dp, color = colorScheme.onBackground)
                    .padding(all = 16.dp),
            )
        },
        contentScale = contentScale,
        contentDescription = null,
    )
}
