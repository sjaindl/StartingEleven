package com.sjaindl.s11.assistant.ui.components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun JumpingDotsIndicator(
    modifier: Modifier = Modifier,
    dotSize: Dp = 10.dp,
    dotColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    jumpHeight: Dp = 10.dp,
    spacing: Dp = 4.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "jumping_dots")

    val yOffsets = (0..2).map { index ->
        val delay = index * 150
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 600,
                    delayMillis = delay,
                    easing = EaseInOut
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "dot_offset_$index"
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        yOffsets.forEach { yOffset ->
            Box(
                Modifier
                    .size(dotSize)
                    .offset(y = -jumpHeight * yOffset.value)
                    .background(
                        color = dotColor,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Preview
@Composable
fun JumpingDotsIndicatorPreview() {
    HvtdpTheme {
        JumpingDotsIndicator()
    }
}
