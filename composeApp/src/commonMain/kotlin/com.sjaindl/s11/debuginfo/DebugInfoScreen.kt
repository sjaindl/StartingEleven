package com.sjaindl.s11.debuginfo

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import startingeleven.composeapp.generated.resources.Res
import startingeleven.composeapp.generated.resources.logo
import startingeleven.composeapp.generated.resources.versionCode
import startingeleven.composeapp.generated.resources.versionName

@Composable
fun DebugInfoScreen() {
    val versionInfo = AppVersionInfo()

    Column(
        modifier = Modifier
            .padding(top = spacing.s),
        verticalArrangement = Arrangement.spacedBy(space = spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var reverse by remember {
            mutableStateOf(value = false)
        }

        val infiniteTransition = rememberInfiniteTransition(label = "S11InfiniteTransition")

        val color by infiniteTransition.animateColor(
            initialValue = colorScheme.primary,
            targetValue = colorScheme.background,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 5000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "AnimatedColor"
        )

        val rotation by infiniteTransition.animateFloat(
            initialValue = if (reverse) 0f else 360f,
            targetValue = if (reverse) 360f else 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
            label = "AnimatedRotation"
        )

        val alphaValue by infiniteTransition.animateFloat(
            initialValue = 0.5f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "AnimatedAlpha"
        )

        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                append(text = "${stringResource(Res.string.versionCode)}: ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(text = "${versionInfo.getVersionCode()}")
            }
        })

        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                append(text = "${stringResource(Res.string.versionName)}: ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(text = versionInfo.getVersionName())
            }
        })

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawRect(color)
                },
            contentAlignment = Alignment.Center,
        ) {

            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "S11 Animation",
                modifier = Modifier
                    .size(size = 200.dp)
                    .graphicsLayer {
                        rotationZ = rotation
                        alpha = alphaValue
                    }
                    .clickable {
                        reverse = !reverse
                    },
                contentScale = ContentScale.FillBounds,
            )
        }
    }
}
