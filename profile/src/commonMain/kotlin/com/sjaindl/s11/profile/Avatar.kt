package com.sjaindl.s11.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.LocalDimensions
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Avatar(
    imageUri: String?,
    modifier: Modifier = Modifier,
    onAddButtonClicked: () -> Unit = { },
) {
    val context = LocalPlatformContext.current
    val spacing = LocalDimensions.current.spacing

    var showError: Throwable? by remember {
        mutableStateOf(value = null)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = colorScheme.surfaceVariant,
                shape = CircleShape,
            )
            .size(size = 96.dp)
            .clickable(
                onClick = onAddButtonClicked,
                indication = rememberRipple(radius = 48.dp),
                interactionSource = remember {
                    MutableInteractionSource()
                },
            ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        ) {
            val imageRequest = ImageRequest.Builder(context = context)
                .data(imageUri)
                .dispatcher(Dispatchers.Default)
                .memoryCacheKey(imageUri.toString())
                .diskCacheKey(imageUri.toString())
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()

            SubcomposeAsyncImage(
                modifier = Modifier
                    .size(120.dp),
                model = imageRequest,
                loading = {
                    LoadingScreen()
                },
                onError = {
                    it.result.throwable
                    //Icon(imageVector = Icons.Default.Error, contentDescription = null)
                    // ErrorScreen!
                },
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
        }

        IconButton(
            onClick = onAddButtonClicked,
            colors = IconButtonDefaults.iconButtonColors(containerColor = colorScheme.secondary),
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.BottomEnd),
        ) {
            Icon(
                imageVector = if (imageUri == null) Icons.Default.Add else Icons.Default.Edit,
                contentDescription = null,
                tint = colorScheme.onSecondary,
                modifier = Modifier
                    .padding(all = if (imageUri == null) spacing.xxs else spacing.xs),
            )
        }
    }

    showError?.let {
        ErrorScreen(
            text = it.message ?: it.toString(),
        )
    }
}

@Preview
@Composable
fun AvatarPreview() {
    HvtdpTheme {
        Avatar(
            imageUri = null,
        )
    }
}
