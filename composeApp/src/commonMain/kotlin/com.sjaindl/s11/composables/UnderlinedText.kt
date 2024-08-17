package com.sjaindl.s11.composables

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.LocalDimensions
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UnderlinedText(
      text: String,
      modifier: Modifier = Modifier,
      trailingIcon: @Composable (() -> Unit)? = null,
) {
   val spacing = LocalDimensions.current.spacing

   Column(
         modifier = modifier,
   ) {
      Row {
         Text(
               text = text,
               style = MaterialTheme.typography.bodyMedium,
         )

         trailingIcon?.let {
            Spacer(modifier = Modifier.weight(weight = 1f))
            it()
         }
      }

      Spacer(
            modifier = Modifier
                  .height(spacing.xxs),
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
                  text = "Sample text",
                  trailingIcon = {
                     Icon(
                           imageVector = Icons.Filled.Check,
                           contentDescription = null,
                     )
                  }
            )
         }
      }
   }
}
