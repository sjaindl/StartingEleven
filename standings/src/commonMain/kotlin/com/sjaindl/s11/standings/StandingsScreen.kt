package com.sjaindl.s11.standings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.firestore.user.model.User
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.standings.model.UserWithPoints
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.standings.generated.resources.Res
import startingeleven.standings.generated.resources.placing

@Composable
fun StandingsScreen(
    standingsState: StandingsState,
    onReload: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (standingsState) {
        StandingsState.Initial -> {
            LoadingScreen()
        }

        is StandingsState.Loading -> {
            LoadingScreen(
                loadingInfo = standingsState.user?.let {
                    stringResource(resource = Res.string.placing, it)
                }
            )
        }

        is StandingsState.Error -> {
            ErrorScreen(
                modifier = modifier,
                text = standingsState.message,
                onButtonClick = onReload,
            )
        }

        is StandingsState.Calculated -> {
            val rankedUsers = standingsState.usersWithPoints.sortedBy {
                it.points
            }.reversed()

            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(all = 16.dp),
            ) {

                itemsIndexed(rankedUsers) { index, userWithPoints ->
                    StandingItem(userWithPoints = userWithPoints, ranking = index + 1)
                }
            }
        }
    }
}

@Preview
@Composable
fun StandingsScreenPreview() {
    HvtdpTheme {
        StandingsScreen(
            standingsState = StandingsState.Calculated(
                usersWithPoints = listOf(
                    UserWithPoints(
                        user = User(
                            uid = "1",
                            email = "df@hvtdp.at",
                            userName = "Daniel Fabian",
                            photoRef = "",
                            photoRefDownloadUrl = "",
                            photoUrl = "",
                            providerId = "GOOGLE",
                            formation = "4-4-2",
                            isAdmin = true,
                        ),
                        points = 20F,
                        pointsLastRound = -5F,
                        betPoints = 0,
                        betPointsLastRound = 0,
                    ),
                    UserWithPoints(
                        user = User(
                            uid = "2",
                            email = "pb@hvtdp.at",
                            userName = "Philippe",
                            photoRef = "",
                            photoRefDownloadUrl = "",
                            photoUrl = "",
                            providerId = "GOOGLE",
                            formation = "4-4-2",
                            isAdmin = true,
                        ),
                        points = 25F,
                        pointsLastRound = 0F,
                        betPoints = 0,
                        betPointsLastRound = 0,
                    ),
                    UserWithPoints(
                        user = User(
                            uid = "3",
                            email = "sj@hvtdp.at",
                            userName = "Stefan Jaindl",
                            photoRef = "",
                            photoRefDownloadUrl = "",
                            photoUrl = "",
                            providerId = "GOOGLE",
                            formation = "4-4-2",
                            isAdmin = true,
                        ),
                        points = 15F,
                        pointsLastRound = 5F,
                        betPoints = 0,
                        betPointsLastRound = 0,
                    ),
                    UserWithPoints(
                        user = User(
                            uid = "123",
                            email = "ll@hvtdp.at",
                            userName = "Lucky Loser",
                            photoRef = "",
                            photoRefDownloadUrl = "",
                            photoUrl = "",
                            providerId = "GOOGLE",
                            formation = "4-4-2",
                            isAdmin = true,
                        ),
                        points = 10F,
                        pointsLastRound = -2F,
                        betPoints = 0,
                        betPointsLastRound = 0,
                    ),
                )
            ),
            onReload = { },
        )
    }
}
