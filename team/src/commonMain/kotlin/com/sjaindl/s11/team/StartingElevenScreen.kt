package com.sjaindl.s11.team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.baseui.expandable.ExpandableCard
import com.sjaindl.s11.core.firestore.bets.model.Bet
import com.sjaindl.s11.core.firestore.player.model.Player
import com.sjaindl.s11.core.firestore.player.model.Player.Companion.MISSING_PLAYER
import com.sjaindl.s11.core.firestore.player.model.Position
import com.sjaindl.s11.core.firestore.userlineup.model.LineupData
import com.sjaindl.s11.core.player.PlayerUI
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.core.theme.spacing
import com.sjaindl.s11.players.PlayerWithLineupCount
import com.sjaindl.s11.team.bet.BetContainer
import com.sjaindl.s11.team.bet.BetState
import com.sjaindl.s11.team.bet.UserBet
import com.sjaindl.s11.team.model.Formation
import com.sjaindl.s11.team.recommender.LineupRecommendation
import com.sjaindl.s11.team.recommender.RecommendationState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.team.generated.resources.Res
import startingeleven.team.generated.resources.attacker
import startingeleven.team.generated.resources.defender
import startingeleven.team.generated.resources.formationTitle
import startingeleven.team.generated.resources.goalKeeper
import startingeleven.team.generated.resources.lineupTitle
import startingeleven.team.generated.resources.midfielder

@Composable
fun StartingElevenScreen(
    startingElevenState: StartingElevenState,
    recommendationState: RecommendationState,
    betState: BetState,
    userBetState: UserBet,
    savedBet: Boolean,
    loadRecommendations: () -> Unit,
    onFormationSelected: (Formation) -> Unit,
    onChoosePlayer: (Position, Int, String) -> Unit, // pos, index, playerID
    loadTeam: () -> Unit,
    resetSavedBetState: () -> Unit,
    setHomeBet: (Int) -> Unit,
    setAwayBet: (Int) -> Unit,
    submitBet: () -> Unit,
    loadBets: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .verticalScroll(state = rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {

        BetContainer(
            betState = betState,
            userBetState = userBetState,
            savedBet = savedBet,
            resetSavedBetState = resetSavedBetState,
            setHomeBet = setHomeBet,
            setAwayBet = setAwayBet,
            submitBet = submitBet,
            loadBets = loadBets,
            onShowSnackBar = onShowSnackBar,
        )

        when (startingElevenState) {
            StartingElevenState.Initial, StartingElevenState.Loading -> {
                LoadingScreen()
            }

            is StartingElevenState.Error -> {
                ErrorScreen(
                    modifier = modifier,
                    text = startingElevenState.message,
                    onButtonClick = {
                        loadTeam()
                    },
                )
            }

            is StartingElevenState.Content -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                ) {

                    with(startingElevenState) {
                        Text(
                            text = stringResource(resource = Res.string.formationTitle),
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                        )

                        FormationChooser(
                            selectedFormation = formation,
                            possibleFormations = possibleFormations,
                            enabled = enabled,
                            onFormationSelected = onFormationSelected,
                            modifier = Modifier
                                .padding(bottom = 16.dp),
                        )

                        Text(
                            text = stringResource(resource = Res.string.lineupTitle),
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                        )

                        Position.entries.forEach { position ->
                            val playersAtPosition by remember(key1 = playersWithLineupCount) {
                                mutableStateOf(
                                    playersWithLineupCount.filter { playerWithLineupCount ->
                                        playerWithLineupCount.player.positions.contains(position)
                                    }
                                )
                            }

                            val numMissingPlayers by remember(
                                key1 = lineup,
                                key2 = formation,
                            ) {
                                mutableStateOf(
                                    when (position) {
                                        Position.Goalkeeper -> if (lineup.goalkeeper == null) 1 else 0
                                        Position.Defender -> formation.defenders - lineup.defenders.size
                                        Position.Midfielder -> formation.midfielders - lineup.midfielders.size
                                        Position.Attacker -> formation.attackers - lineup.attackers.size
                                    }
                                )
                            }

                            val linedUpPlayersAtPosition by remember(
                                key1 = lineup,
                                key2 = numMissingPlayers
                            ) {
                                mutableStateOf(
                                    when (position) {
                                        Position.Goalkeeper -> lineup.goalkeeper?.let {
                                            listOf(it)
                                        } ?: emptyList()

                                        Position.Defender -> lineup.defenders
                                        Position.Midfielder -> lineup.midfielders
                                        Position.Attacker -> lineup.attackers
                                    }.toMutableList().apply {
                                        repeat(times = numMissingPlayers) {
                                            add(element = MISSING_PLAYER)
                                        }
                                    }
                                )
                            }

                            val numOfRequiredPlayersAtPosition by remember(
                                key1 = lineup,
                                key2 = formation,
                            ) {
                                mutableStateOf(
                                    when (position) {
                                        Position.Goalkeeper -> 1
                                        Position.Defender -> formation.defenders
                                        Position.Midfielder -> formation.midfielders
                                        Position.Attacker -> formation.attackers
                                    }
                                )
                            }

                            val lineupCount by remember(
                                numOfRequiredPlayersAtPosition,
                                numMissingPlayers
                            ) {
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
                                            season = season,
                                            lineupCount = playerWithLineupCount?.lineupCount,
                                            possiblePlayers = playersAtPosition.filter {
                                                // prevent choosing same player twice (at same or different position)
                                                it.player.playerId !in (lineup.allLinedUpPlayers - playerWithLineupCount?.player?.playerId)
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
            }
        }

        LineupRecommendation(
            recommendationState = recommendationState,
            loadRecommendations = loadRecommendations,
        )
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
                positions = listOf(Position.Goalkeeper),
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
                positions = listOf(Position.Goalkeeper),
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
                positions = listOf(Position.Defender),
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
                positions = listOf(Position.Defender),
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
                positions = listOf(Position.Midfielder),
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
                positions = listOf(Position.Midfielder),
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
                positions = listOf(Position.Attacker),
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
                positions = listOf(Position.Attacker),
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
                positions = listOf(Position.Attacker),
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
        StartingElevenScreen(
            startingElevenState = StartingElevenState.Content(
                season = "2024",
                possibleFormations = possibleFormations,
                formation = selectedFormation,
                playersWithLineupCount = playerWithLineupCount,
                lineup = LineupData(
                    goalkeeper = null,
                    defenders = emptyList(),
                    midfielders = emptyList(),
                    attackers = emptyList(),
                ),
                enabled = true,
            ),
            recommendationState = RecommendationState.NoRecommendation,
            betState = BetState.Content(
                Bet(
                    id = "1",
                    home = "HVTDP Stainz",
                    away = "Sturm Graz",
                    resultScoreHome = null,
                    resultScoreAway = null,
                ),
                enabled = true,
            ),
            userBetState = UserBet(homeBet = 1, awayBet = 1),
            savedBet = false,
            loadRecommendations = { },
            onFormationSelected = {
                selectedFormation = it
            },
            onChoosePlayer = { _, _, _ ->
            },
            loadTeam = { },
            resetSavedBetState = { },
            setHomeBet = { },
            setAwayBet = { },
            submitBet = { },
            loadBets = { },
            onShowSnackBar = { },
        )
    }
}
