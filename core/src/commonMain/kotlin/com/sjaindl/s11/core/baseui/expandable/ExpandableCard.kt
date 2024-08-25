package com.sjaindl.s11.core.baseui.expandable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.sjaindl.s11.core.baseui.S11Card
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val EXPAND_ANIMATION_DURATION = 400

@Composable
fun ExpandableCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var expanded by remember {
        mutableStateOf(value = false)
    }

    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = rememberTransition(transitionState = transitionState, label = "transition")

    val arrowRotationDegree by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = EXPAND_ANIMATION_DURATION)
        },
        label = "rotationDegreeTransition",
    ) {
        if (expanded) 0f else -180f
    }

    S11Card(
        modifier = modifier
            .fillMaxWidth(),
        isElevated = true,
        onClick = {
            expanded = !expanded
        },
        backgroundColor = colorScheme.surfaceContainer
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = EXPAND_ANIMATION_DURATION,
                        easing = FastOutSlowInEasing,
                    )
                ),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    modifier = Modifier.weight(6f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Visible,
                )

                CardArrow(
                    degrees = arrowRotationDegree,
                    onClick = {
                        expanded = !expanded
                    },
                    modifier = Modifier
                        .alpha(.5f),
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(spring(
                    stiffness = Spring.StiffnessHigh,
                    dampingRatio = Spring.DampingRatioHighBouncy,
                )) + expandVertically(),
                exit = fadeOut(spring(
                    stiffness = Spring.StiffnessHigh,
                    dampingRatio = Spring.DampingRatioHighBouncy
                )) + shrinkVertically()
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun ExpandableCardPreview() {
    HvtdpTheme {
        ExpandableCard(
            title = "Expandable Content",
            content = {
                Text("Some content to expand or shrink")
            }
        )
    }
}
