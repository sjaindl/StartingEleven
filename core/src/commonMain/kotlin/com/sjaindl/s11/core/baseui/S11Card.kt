package com.sjaindl.s11.core.baseui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.LocalDimensions
import com.sjaindl.s11.core.theme.cardShape
import com.sjaindl.s11.core.theme.spacing
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun S11Card(
      modifier: Modifier = Modifier,
      backgroundColor: Color? = null,
      onClick: (() -> Unit)? = null,
      onLongClick: (() -> Unit)? = null,
      isElevated: Boolean = false,
      contentPadding: PaddingValues = PaddingValues(spacing.md),
      verticalArrangement: Arrangement.Vertical = Arrangement.Top,
      horizontalAlignment: Alignment.Horizontal = Alignment.Start,
      content: @Composable ColumnScope.() -> Unit,
) {
   var finalModifier = modifier

   if (isElevated) {
      finalModifier = finalModifier.shadow(
            elevation = LocalDimensions.current.cardDefaultElevation,
            shape = cardShape(),
            clip = onClick != null,
      )
   }

   if (onClick != null && onLongClick != null) {
      finalModifier = finalModifier
          .clip(cardShape())
          .combinedClickable(
              onClick = onClick,
              onLongClick = onLongClick,
          )
   } else if (onClick != null) {
      finalModifier = finalModifier
            .clip(cardShape())
            .clickable(onClick = onClick)
   }

   Column(
         verticalArrangement = verticalArrangement,
         horizontalAlignment = horizontalAlignment,
         modifier = finalModifier
               .background(
                     color = backgroundColor ?: colorScheme.surface,
                     shape = cardShape()
               )
               .padding(contentPadding)
   ) {
      CompositionLocalProvider(
            LocalContentColor provides colorScheme.onSurface,
      ) {
         content()
      }
   }
}


@Preview
@Composable
private fun S11CardPreview() {
   HvtdpTheme {
      Column {
          S11Card(
               isElevated = true,
               modifier = Modifier
                     .fillMaxWidth()
                     .padding(8.dp)
         ) {
            Text("One")
            Text("Two")
            Text("Three")
         }

          S11Card(
               isElevated = true,
               onClick = {},
               modifier = Modifier
                     .fillMaxWidth()
                     .padding(8.dp)
         ) {
            Text("one clickable")
            Text("Two clickable")
            Text("Three clickable")
         }

          S11Card(
               modifier = Modifier
                     .fillMaxWidth()
                     .padding(8.dp)
         ) {
            Text("one")
            Text("Two")
            Text("Three")
         }
      }

   }
}

