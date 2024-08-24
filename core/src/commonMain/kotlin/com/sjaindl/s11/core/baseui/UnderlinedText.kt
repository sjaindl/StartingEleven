package com.sjaindl.s11.core.baseui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UnderlinedText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val color = if (enabled) {
        LocalTextStyle.current.color
    } else {
        colorScheme.onSurface.copy(alpha = 0.38f)
    }

    UnderlinedTextInternal(
        textContent = {
            Text(
                text = text,
                color = color,
                style = typography.bodyLarge,
            )
        },
        modifier = modifier,
        trailingIcon = trailingIcon,
    )
}

@Composable
fun UnderlinedText(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val color = if (enabled) {
        colorScheme.onSurface
    } else {
        colorScheme.onSurface.copy(alpha = 0.38f)
    }

    UnderlinedTextInternal(
        textContent = {
            Text(
                text = text,
                color = color,
                style = typography.bodyLarge,
            )
        },
        modifier = modifier,
        trailingIcon = trailingIcon,
    )
}

@Composable
private fun UnderlinedTextInternal(
    textContent: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier,
    ) {
        Row {
            textContent()

            trailingIcon?.let {
                Spacer(modifier = Modifier.weight(weight = 1f))
                it()
            }
        }

        Spacer(
            modifier = Modifier
                .height(4.dp),
        )

       HorizontalDivider(
           modifier = modifier
               .fillMaxWidth(),
           thickness = 1.dp,
       )
   }
}

@Composable
@Preview
fun UnderlinedTextPreview() {
    HvtdpTheme {
        CompositionLocalProvider(
            value = LocalContentColor provides colorScheme.onBackground,
        ) {
            Box(
                modifier = Modifier.padding(32.dp),
            ) {
                UnderlinedText(
                    text = "Some test text",
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                        )
                    },
                )
            }
        }
    }
}
