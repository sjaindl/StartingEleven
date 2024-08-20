package com.sjaindl.s11.profile

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
import startingeleven.profile.generated.resources.Res
import startingeleven.profile.generated.resources.profileChoosePhoto
import startingeleven.profile.generated.resources.profileDeletePhoto
import startingeleven.profile.generated.resources.profileTakePhoto

@Composable
fun PhotoPickerBottomSheet(
    onDismiss: () -> Unit,
    onDeleteImage: () -> Unit,
    // photoCaptureRequest: PhotoPickerResult,
    // photoPickerRequest: PhotoPickerResult,
    canDeleteImage: Boolean,
) {
    val dimensions = LocalDimensions.current

    S11BottomSheet(
        onDismissBottomSheet = onDismiss
    ) {
        Button(
            onClick = {
                //photoCaptureRequest.launchPicker()
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
                //photoPickerRequest.launchPicker()
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
                text = stringResource(resource = Res.string.profileDeletePhoto),
            )
        }

        if (canDeleteImage) {
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
                    onDeleteImage()
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
