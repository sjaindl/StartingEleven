package com.sjaindl.s11.core.baseui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.model.TextDropdownMenuItem
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextDropdown(
    text: String,
    menuItems: List<TextDropdownMenuItem>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var dropdownMenuExpanded by remember {
        mutableStateOf(value = false)
    }

    ExposedDropdownMenuBox(
        expanded = dropdownMenuExpanded,
        onExpandedChange = {
           // dropdownMenuExpanded = !dropdownMenuExpanded
        },
        modifier = modifier
            .fillMaxSize()
            .clickable {
                dropdownMenuExpanded = !dropdownMenuExpanded
            }
    ) {
        UnderlinedText(
            text = text,
            modifier = Modifier
                .menuAnchor(),
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .requiredSize(24.dp),
                ) {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownMenuExpanded)
                }
            }
        )

        ExposedDropdownMenu(
            expanded = dropdownMenuExpanded,
            onDismissRequest = {
               // dropdownMenuExpanded = false
            },
        ) {
            menuItems.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row {
                            Text(
                                text = item.text,
                                style = typography.bodyLarge,
                            )

                            if (item.checked) {
                                Spacer(modifier = Modifier.weight(weight = 1f))
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .requiredSize(24.dp),
                                )
                            }
                        }
                    },
                    onClick = {
                        onItemSelected(item.text)
                        dropdownMenuExpanded = false
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
@Preview
fun TextDropdownPreview() {
    HvtdpTheme {
        CompositionLocalProvider(
            value = LocalContentColor provides colorScheme.onBackground,
        ) {
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .padding(32.dp),
            ) {
                TextDropdown(
                    text = "Test dropdown",
                    menuItems = listOf(
                        TextDropdownMenuItem(text = "Item 1", checked = false),
                        TextDropdownMenuItem(text = "Item 2", checked = true),
                        TextDropdownMenuItem(text = "Item 3", checked = false),
                    ),
                    onItemSelected = { },
                )
            }
        }
    }
}
