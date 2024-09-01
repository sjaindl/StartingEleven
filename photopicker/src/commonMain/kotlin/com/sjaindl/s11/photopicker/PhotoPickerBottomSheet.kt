package com.sjaindl.s11.photopicker

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sjaindl.s11.core.baseui.S11BottomSheet
import com.sjaindl.s11.core.theme.LocalDimensions
import org.jetbrains.compose.resources.stringResource
import startingeleven.photopicker.generated.resources.Res
import startingeleven.photopicker.generated.resources.profileChoosePhoto
import startingeleven.photopicker.generated.resources.profileDeletePhoto
import startingeleven.photopicker.generated.resources.profileTakePhoto

@Composable
fun PhotoPickerBottomSheet(
    onDismiss: () -> Unit,
    onPickPhoto: () -> Unit,
    onTakePhoto: () -> Unit,
    onDeletePhoto: () -> Unit,
    canDeletePhoto: Boolean,
) {
    val dimensions = LocalDimensions.current

    S11BottomSheet(
        onDismissBottomSheet = onDismiss
    ) {
        Button(
            onClick = {
                onTakePhoto()
                onDismiss()
            },
            Modifier
                .padding(
                    horizontal = dimensions.spacing.md,
                    vertical = dimensions.spacing.xs
                )
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(resource = Res.string.profileTakePhoto),
            )
        }

        Button(
            onClick = {
                onPickPhoto()
                onDismiss()
            },
            modifier = Modifier
                .padding(
                    horizontal = dimensions.spacing.md,
                    vertical = dimensions.spacing.xs
                )
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(resource = Res.string.profileChoosePhoto),
            )
        }

        if (canDeletePhoto) {
            HorizontalDivider(
                Modifier
                    .padding(
                        horizontal = dimensions.spacing.md,
                        vertical = dimensions.spacing.xs
                    )
                    .fillMaxWidth()
            )
            Button(
                onClick = {
                    onDeletePhoto()
                    onDismiss()
                },
                modifier = Modifier
                    .padding(
                        horizontal = dimensions.spacing.md,
                        vertical = dimensions.spacing.xs
                    )
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(resource = Res.string.profileDeletePhoto),
                )
            }
        }
        Spacer(
            modifier = Modifier
                .padding(bottom = dimensions.spacing.md),
        )
    }
}
