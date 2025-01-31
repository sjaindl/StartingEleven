package com.sjaindl.s11.team.recommender

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.firestore.player.model.Player
import com.sjaindl.s11.core.firestore.player.model.Position
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.team.generated.resources.Res
import startingeleven.team.generated.resources.football_field
import startingeleven.team.generated.resources.noRecommendation
import startingeleven.team.generated.resources.recommendationTitle

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LineupRecommendation(
    recommendationState: RecommendationState,
    loadRecommendations: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (recommendationState) {
        RecommendationState.Initial, RecommendationState.Loading -> {
            LoadingScreen()
        }

        is RecommendationState.Error -> {
            ErrorScreen(
                modifier = modifier,
                text = recommendationState.message,
                onButtonClick = loadRecommendations,
            )
        }

        RecommendationState.NoRecommendation -> {
            Text(text = stringResource(resource = Res.string.noRecommendation))
        }

        is RecommendationState.Recommendation -> {
            Text(
                text = stringResource(resource = Res.string.recommendationTitle),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                style = typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )

            Box(
                modifier = modifier
                    .fillMaxSize(),
            ) {
                Image(
                    painter = painterResource(Res.drawable.football_field),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillWidth,
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    SmallPlayerUI(
                        player = recommendationState.goalkeeper,
                        modifier = Modifier
                            .fillMaxSize(),
                    )

                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        recommendationState.defenders.forEach { player ->
                            SmallPlayerUI(player = player)
                        }
                    }

                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        recommendationState.midfielders.forEach { player ->
                            SmallPlayerUI(player = player)
                        }
                    }

                    FlowRow(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        recommendationState.attackers.forEach { player ->
                            SmallPlayerUI(player = player)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LineupRecommendationPreview() {
    val player = Player(
        playerId = "inzaghi",
        name = "Inzaghi",
        position = Position.Attacker,
        imageRef = null,
        downloadUrl = null,
        points = mapOf("inzaghi" to 4f),
    )

    HvtdpTheme {
        LineupRecommendation(
            recommendationState = RecommendationState.Recommendation(
                goalkeeper = player,
                defenders = listOf(player, player, player, player),
                midfielders = listOf(player, player, player),
                attackers = listOf(player, player, player),
            ),
            loadRecommendations = { },
        )
    }
}
