package com.sjaindl.s11.team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.baseui.expandable.ExpandableCard
import com.sjaindl.s11.core.firestore.player.model.Player
import com.sjaindl.s11.core.firestore.player.model.Player.Companion.MISSING_PLAYER
import com.sjaindl.s11.core.firestore.player.model.Position
import com.sjaindl.s11.core.firestore.userlineup.model.LineupData
import com.sjaindl.s11.core.player.PlayerUI
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import com.sjaindl.s11.players.PlayerWithLineupCount
import com.sjaindl.s11.team.model.Formation
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.team.generated.resources.Res
import startingeleven.team.generated.resources.attacker
import startingeleven.team.generated.resources.defender
import startingeleven.team.generated.resources.goalKeeper
import startingeleven.team.generated.resources.midfielder

@Composable
fun StartingElevenScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = viewModel {
        StartingElevenViewModel()
    }

    val startingElevenState by viewModel.startingElevenState.collectAsState()

    when (val state = startingElevenState) {
        StartingElevenState.Initial, StartingElevenState.Loading -> {
            LoadingScreen()
        }

        is StartingElevenState.Error -> {
            ErrorScreen(
                modifier = modifier,
                text = state.message,
                onButtonClick = {
                    viewModel.loadTeam()
                },
            )
        }

        is StartingElevenState.Content -> {
            StartingElevenScreenContent(
                playersWithLineupCount = state.playersWithLineupCount,
                lineupData = state.lineup,
                selectedFormation = state.formation,
                possibleFormations = state.possibleFormations,
                enabled = state.enabled,
                onFormationSelected = viewModel::onFormationSelected,
                onChoosePlayer = viewModel::onChoosePlayer,
            )
        }
    }
}

@Composable
fun StartingElevenScreenContent(
    playersWithLineupCount: List<PlayerWithLineupCount>,
    lineupData: LineupData,
    selectedFormation: Formation,
    possibleFormations: List<Formation>,
    enabled: Boolean,
    onFormationSelected: (Formation) -> Unit,
    onChoosePlayer: (Position, Int, String) -> Unit, // pos, index, playerID
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(state = rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {

        FormationChooser(
            selectedFormation = selectedFormation,
            possibleFormations = possibleFormations,
            enabled = enabled,
            onFormationSelected = onFormationSelected,
            modifier = Modifier
                .padding(bottom = 16.dp),
        )

        Position.entries.forEach { position ->
            val playersAtPosition by remember(key1 = playersWithLineupCount) {
                mutableStateOf(
                    playersWithLineupCount.filter { playerWithLineupCount ->
                        playerWithLineupCount.player.position == position
                    }
                )
            }

            val numMissingPlayers by remember(key1 = lineupData, key2 = selectedFormation) {
                mutableStateOf(
                    when (position) {
                        Position.Goalkeeper -> if (lineupData.goalkeeper == null) 1 else 0
                        Position.Defender -> selectedFormation.defenders - lineupData.defenders.size
                        Position.Midfielder -> selectedFormation.midfielders - lineupData.midfielders.size
                        Position.Attacker -> selectedFormation.attackers - lineupData.attackers.size
                    }
                )
            }

            val linedUpPlayersAtPosition by remember(key1 = lineupData, key2 = numMissingPlayers) {
                mutableStateOf(
                    when (position) {
                        Position.Goalkeeper -> lineupData.goalkeeper?.let {
                            listOf(it)
                        } ?: emptyList()
                        Position.Defender -> lineupData.defenders
                        Position.Midfielder -> lineupData.midfielders
                        Position.Attacker -> lineupData.attackers
                    }.toMutableList().apply {
                        repeat(times = numMissingPlayers) {
                            add(element = MISSING_PLAYER)
                        }
                    }
                )
            }

            val numOfRequiredPlayersAtPosition by remember(key1 = lineupData, key2 = selectedFormation) {
                mutableStateOf(
                    when (position) {
                        Position.Goalkeeper -> 1
                        Position.Defender -> selectedFormation.defenders
                        Position.Midfielder -> selectedFormation.midfielders
                        Position.Attacker -> selectedFormation.attackers
                    }
                )
            }

            val lineupCount by remember(numOfRequiredPlayersAtPosition, numMissingPlayers) {
                mutableStateOf(value = "(${numOfRequiredPlayersAtPosition - numMissingPlayers}/$numOfRequiredPlayersAtPosition)")
            }

            val title = when (position) {
                Position.Goalkeeper -> stringResource(Res.string.goalKeeper) + " $lineupCount"
                Position.Defender -> stringResource(Res.string.defender) + " $lineupCount"
                Position.Midfielder -> stringResource(Res.string.midfielder) + " $lineupCount"
                Position.Attacker -> stringResource(Res.string.attacker) + " $lineupCount"
            }

            ExpandableCard(
                title = title,
                initiallyExpanded = numMissingPlayers > 0
            ) {
                Column(
                    modifier = Modifier
                        .padding(all = spacing.xs),
                ) {
                    linedUpPlayersAtPosition.forEachIndexed { index, linedUpPlayerId ->
                        val playerWithLineupCount = playersWithLineupCount.firstOrNull {
                            it.player.playerId == linedUpPlayerId
                        }

                        PlayerUI(
                            player = playerWithLineupCount?.player,
                            lineupCount = playerWithLineupCount?.lineupCount,
                            possiblePlayers = playersAtPosition.filter {
                                // prevent choosing same player twice
                                it.player.playerId !in (linedUpPlayersAtPosition - playerWithLineupCount?.player?.playerId)
                            }.map {
                                it.player
                            },
                            displayDropdown = enabled,
                            onPlayerSelected = {
                                onChoosePlayer(position, index, it.playerId)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun StartingElevenScreenPreview() {
    val playerWithLineupCount = listOf(
        PlayerWithLineupCount(
            Player(
                playerId = "1",
                name = "Gigi Buffon",
                position = Position.Goalkeeper,
                imageRef = null,
                downloadUrl = null,
                points = emptyMap(),
            ),
            lineupCount = 1,
        ),
        PlayerWithLineupCount(
            Player(
                playerId = "2",
                name = "Gigi Donnaruma",
                position = Position.Goalkeeper,
                imageRef = null,
                downloadUrl = null,
                points = emptyMap(),
            ),
            lineupCount = 2,
        ),
        PlayerWithLineupCount(
            Player(
                playerId = "3",
                name = "Paolo Maldini",
                position = Position.Defender,
                imageRef = null,
                downloadUrl = null,
                points = emptyMap(),
            ),
            lineupCount = 3,
        ),
        PlayerWithLineupCount(
            Player(
                playerId = "4",
                name = "Gregory Wüthrich",
                position = Position.Defender,
                imageRef = null,
                downloadUrl = null,
                points = emptyMap(),
            ),
            lineupCount = 4,
        ),
        PlayerWithLineupCount(
            Player(
                playerId = "5",
                name = "Jakob Jantscher",
                position = Position.Midfielder,
                imageRef = null,
                downloadUrl = null,
                points = emptyMap(),
            ),
            lineupCount = 5,
        ),
        PlayerWithLineupCount(
            Player(
                playerId = "6",
                name = "Otar Kiteischvili",
                position = Position.Midfielder,
                imageRef = null,
                downloadUrl = null,
                points = emptyMap(),
            ),
            lineupCount = 6,
        ),
        PlayerWithLineupCount(
            Player(
                playerId = "7",
                name = "Alessandro Del Piero",
                position = Position.Attacker,
                imageRef = null,
                downloadUrl = null,
                points = emptyMap(),
            ),
            lineupCount = 7,
        ),
        PlayerWithLineupCount(
            Player(
                playerId = "8",
                name = "Francesco Totti",
                position = Position.Attacker,
                imageRef = null,
                downloadUrl = null,
                points = emptyMap(),
            ),
            lineupCount = 8,
        ),
        PlayerWithLineupCount(
            Player(
                playerId = "9",
                name = "Ivica Vastic",
                position = Position.Attacker,
                imageRef = null,
                downloadUrl = null,
                points = emptyMap(),
            ),
            lineupCount = 9,
        ),
    )

    var selectedFormation by remember {
        mutableStateOf(Formation.defaultFormation)
    }

    val possibleFormations = listOf(
        Formation.defaultFormation,
        Formation(formationId = "3-4-3", defenders = 3, midfielders = 4, attackers = 3),
    )

    HvtdpTheme {
        StartingElevenScreenContent(
            playersWithLineupCount = playerWithLineupCount,
            lineupData = LineupData(
                goalkeeper = null,
                defenders = emptyList(),
                midfielders = emptyList(),
                attackers = emptyList(),
            ),
            selectedFormation = selectedFormation,
            possibleFormations = possibleFormations,
            enabled = true,
            onFormationSelected = {
                selectedFormation = it
            },
            onChoosePlayer = { _, _, _ ->
            }
        )
    }
}
