package com.sjaindl.s11.team

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.baseui.TextDropdown
import com.sjaindl.s11.core.baseui.UnderlinedText
import com.sjaindl.s11.core.model.TextDropdownMenuItem
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.team.model.Formation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FormationChooser(
    selectedFormation: Formation,
    possibleFormations: List<Formation>,
    enabled: Boolean,
    onFormationSelected: (Formation) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (enabled) {
        TextDropdown(
            text = selectedFormation.formationId,
            menuItems = possibleFormations.map {
                TextDropdownMenuItem(
                    text = it.formationId,
                    checked = it.formationId == selectedFormation.formationId,
                )
            },
            onItemSelected = { formationId ->
                onFormationSelected(
                    possibleFormations.firstOrNull {
                        it.formationId == formationId
                    } ?: Formation.defaultFormation
                )
            },
            modifier = modifier,
        )
    } else {
        UnderlinedText(
            text = selectedFormation.formationId,
            modifier = modifier,
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

@Preview
@Composable
fun FormationChooserPreview() {
    var selectedFormation by remember {
        mutableStateOf(Formation.defaultFormation)
    }

    val possibleFormations = listOf(
        Formation.defaultFormation,
        Formation(formationId = "3-4-3", defenders = 3, midfielders = 4, attackers = 3),
    )

    HvtdpTheme {
        CompositionLocalProvider(
            value = LocalContentColor provides colorScheme.onBackground,
        ) {
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .padding(32.dp),
            ) {

                FormationChooser(
                    selectedFormation = selectedFormation,
                    possibleFormations = possibleFormations,
                    enabled = true,
                    onFormationSelected = {
                        selectedFormation = it
                    },
                )
            }
        }
    }
}
