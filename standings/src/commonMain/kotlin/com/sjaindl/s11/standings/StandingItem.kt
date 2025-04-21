package com.sjaindl.s11.standings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sjaindl.s11.core.baseui.FallbackImage
import com.sjaindl.s11.core.baseui.S11Card
import com.sjaindl.s11.core.baseui.UnderlinedText
import com.sjaindl.s11.core.firestore.user.model.User
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.standings.model.UserWithPoints
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.standings.generated.resources.Res
import startingeleven.standings.generated.resources.bets
import startingeleven.standings.generated.resources.total

@Composable
fun StandingItem(
    userWithPoints: UserWithPoints,
    ranking: Int,
    modifier: Modifier = Modifier,
) {
    val user = userWithPoints.user
    val scoredLastRound = userWithPoints.pointsLastRound >= 0
    val scoredBetPointsLastRound = userWithPoints.betPointsLastRound > 0
    val defaultColor = LocalTextStyle.current.color
    val primary = colorScheme.primary.copy(alpha = 0.25f)

    val pointsInfo = buildAnnotatedString {
        append("${stringResource(Res.string.total)}: ${userWithPoints.points} (")
        withStyle(
            style = SpanStyle(
                color = if (scoredLastRound) {
                    defaultColor
                } else {
                    colorScheme.error
                },
            )
        ) {
            if (scoredLastRound) {
                append("+")
            }
            append("${userWithPoints.pointsLastRound}")
        }
        append(")")
    }

    val betPointsInfo = buildAnnotatedString {
        append("${stringResource(Res.string.bets)}: ${userWithPoints.betPoints} (")
        withStyle(
            style = SpanStyle(
                color = if (scoredBetPointsLastRound) {
                    Color.Green
                } else {
                    defaultColor
                },
            )
        ) {
            if (scoredBetPointsLastRound) {
                append("+")
            }
            append("${userWithPoints.betPointsLastRound}")
        }
        append(")")
    }

    S11Card(
        backgroundColor = colorScheme.surfaceContainer,
        modifier = modifier
            .padding(vertical = 8.dp),
        isElevated = true,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopEnd,
        ) {
            Text(
                text = "$ranking",
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(
                        top = 12.dp,
                        end = if (ranking / 10 == 0) 18.dp else 12.dp,
                    )
                    .drawBehind {
                        drawCircle(
                            color = when (ranking) {
                                1 -> Color(0xFFFFD700)
                                2 -> Color(0xFFC0C0C0)
                                3 -> Color(0xFFBF8970)
                                else -> primary
                            },
                            radius = size.maxDimension / 1.5f,
                        )
                    },
                textAlign = TextAlign.End,
            )
        }

        Row(
            modifier = Modifier
                .padding(8.dp)
                .background(color = colorScheme.surfaceContainer),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FallbackImage(
                photoRefDownloadUrl = userWithPoints.user.photoRefDownloadUrl,
                photoUrl = userWithPoints.user.photoUrl,
                additionalCacheKey = userWithPoints.user.profilePhotoRefTimestamp,
                maxSize = 120.dp,
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                UnderlinedText(
                    text = user.userName,
                )

                UnderlinedText(
                    text = pointsInfo,
                )

                UnderlinedText(
                    text = betPointsInfo
                )
            }
        }
    }
}

@Preview
@Composable
fun StandingItemPreview() {
    HvtdpTheme {
        StandingItem(
            UserWithPoints(
                user = User(
                    uid = "1",
                    email = "df@hvtdp.at",
                    userName = "Daniel Fabian",
                    photoRef = "",
                    photoRefDownloadUrl = "",
                    profilePhotoRefTimestamp = null,
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
            ranking = 1,
        )
    }
}
