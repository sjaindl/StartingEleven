package com.sjaindl.s11.core.baseui.expandable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HvtdpTheme {
        IconButton(
            modifier = modifier,
            onClick = onClick,
            content = {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(degrees),
                )
            }
        )
    }
}

@Preview
@Composable
fun CardArrowPreview() {
    CardArrow(
        degrees = 0f,
        onClick = { },
    )
}
