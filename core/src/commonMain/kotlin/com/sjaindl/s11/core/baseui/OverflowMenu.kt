package com.sjaindl.s11.core.baseui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.model.OverFlowMenuItem
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.core.generated.resources.Res
import startingeleven.core.generated.resources.showMore

@Composable
fun OverflowMenu(
    menuItems: List<OverFlowMenuItem>,
    modifier: Modifier = Modifier,
) {
   var overflowMenuExpanded by remember {
      mutableStateOf(value = false)
   }

   Box(
         modifier = modifier
               .wrapContentSize(Alignment.TopEnd),
   ) {
      IconButton(
            onClick = {
               overflowMenuExpanded = !overflowMenuExpanded
            },
      ) {
         Icon(
               imageVector = Icons.Default.MoreVert,
               contentDescription = stringResource(Res.string.showMore),
               tint = colorScheme.onPrimary,
         )
      }

      DropdownMenu(
            expanded = overflowMenuExpanded,
            onDismissRequest = {
               overflowMenuExpanded = false
            },
      ) {
         menuItems.forEach {
            DropdownMenuItem(
                  text = {
                     Text(text = it.text)
                  },
                  onClick = {
                     overflowMenuExpanded = false
                     it.onClick()
                  },
                  contentPadding = PaddingValues(end = spacing.xl),
                  leadingIcon = it.icon,
            )
         }
      }
   }
}

@Preview
@Composable
fun OverflowMenuPreview() {
   HvtdpTheme {
      CompositionLocalProvider(
            value = LocalContentColor provides colorScheme.onBackground,
      ) {
         Box(
               modifier = Modifier
                     .height(300.dp)
                     .padding(start = 128.dp)
                     .padding(end = 8.dp),
         ) {
            OverflowMenu(
                  menuItems = listOf(
                      OverFlowMenuItem(
                              text = "Item 1",
                              onClick = { },
                              icon = {
                                 Icon(Icons.Filled.Refresh, contentDescription = null)
                              },
                        ),
                      OverFlowMenuItem(
                              text = "Item 2",
                              onClick = { },
                              icon = {
                                 Icon(Icons.Filled.Close, contentDescription = null)
                              },
                        ),
                      OverFlowMenuItem(
                              text = "Item 3",
                              onClick = { },
                              icon = {
                                 Icon(Icons.Filled.Image, contentDescription = null)
                              },
                        ),
                  ),
            )
         }
      }
   }
}
