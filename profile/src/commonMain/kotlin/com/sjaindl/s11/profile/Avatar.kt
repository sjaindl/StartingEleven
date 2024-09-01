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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.baseui.FallbackImage
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.LocalDimensions
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Avatar(
    profileImageUri: String?,
    profilePhotoRefImageUri: String?,
    profilePhotoRefTimestamp: String?,
    modifier: Modifier = Modifier,
    onAddButtonClicked: () -> Unit = { },
) {
    val spacing = LocalDimensions.current.spacing

    val hasImage = profileImageUri != null || profilePhotoRefImageUri != null

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
                .clip(shape = CircleShape)
        ) {
            FallbackImage(
                photoRefDownloadUrl = profilePhotoRefImageUri,
                photoUrl = profileImageUri,
                additionalCacheKey = profilePhotoRefTimestamp,
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
                imageVector = if (!hasImage) Icons.Default.Add else Icons.Default.Edit,
                contentDescription = null,
                tint = colorScheme.onSecondary,
                modifier = Modifier
                    .padding(all = if (!hasImage) spacing.xxs else spacing.xs),
            )
        }
    }
}

@Preview
@Composable
fun AvatarPreview() {
    HvtdpTheme {
        Avatar(
            profileImageUri = null,
            profilePhotoRefImageUri = null,
            profilePhotoRefTimestamp = null,
        )
    }
}
