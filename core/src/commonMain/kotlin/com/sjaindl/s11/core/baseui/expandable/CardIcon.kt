package com.sjaindl.s11.core.baseui.expandable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.sjaindl.s11.core.theme.HvtdpTheme

@Composable
fun CardIcon(
    modifier: Modifier,
    image: ImageVector,
    onClick: () -> Unit
) {
    HvtdpTheme {
        IconButton(
            modifier = modifier,
            onClick = onClick,
            content = {
                Icon(
                    imageVector = image,
                    contentDescription = null,
                )
            }
        )
    }
}

@Preview
@Composable
fun CardIconPreview() {
    CardIcon(
        modifier = Modifier,
        image = Icons.Default.Add,
        onClick = { },
    )
}
