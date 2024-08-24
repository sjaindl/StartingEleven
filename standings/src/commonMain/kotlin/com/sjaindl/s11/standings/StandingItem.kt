package com.sjaindl.s11.standings

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.baseui.UnderlinedText
import com.sjaindl.s11.core.firestore.user.model.User
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.standings.model.UserWithPoints
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.standings.generated.resources.Res
import startingeleven.standings.generated.resources.ic_user

@Composable
fun StandingItem(
    userWithPoints: UserWithPoints,
    ranking: Int,
    modifier: Modifier = Modifier,
) {
    val context = LocalPlatformContext.current
    val user = userWithPoints.user
    val url = userWithPoints.user.photoUrl
    val scoredLastRound = userWithPoints.pointsLastRound >= 0
    val defaultColor = LocalTextStyle.current.color

    val pointsInfo = buildAnnotatedString {
        append("${userWithPoints.points} (")
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

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopEnd,
    ) {
        val rankingString = buildAnnotatedString {
            val color = when (ranking) {
                1 -> Color(0xFFFFD700)
                2 -> Color(0xFFC0C0C0)
                3 -> Color(0xFFBF8970)
                else -> defaultColor
            }

            withStyle(
                style = SpanStyle(
                    fontSize = 24.sp,
                    color = color,
                )
            ) {
                append("$ranking")
            }
        }

        val primary = colorScheme.primary.copy(alpha = 0.25f)

        Text(
            text = rankingString,
            modifier = Modifier
                .padding(
                    top = 12.dp,
                    end = if (ranking / 10 == 0) 18.dp else 12.dp,
                )
                .drawBehind {
                    drawCircle(
                        color = primary,
                        radius = size.maxDimension / 1.5f,
                    )
                },
            textAlign = TextAlign.End,
        )
    }

    Row(
        modifier = modifier
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (url != null) {
            val imageRequest = ImageRequest.Builder(context = context)
                .data(url)
                .dispatcher(Dispatchers.Default)
                .memoryCacheKey(url)
                .diskCacheKey(url)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()

            SubcomposeAsyncImage(
                modifier = Modifier
                    .size(size = 120.dp),
                model = imageRequest,
                loading = {
                    LoadingScreen(
                        modifier = Modifier
                            .size(size = 120.dp)
                            .border(width = 1.dp, color = colorScheme.onBackground)
                            .padding(all = 16.dp),
                    )
                },
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )
        } else {
            Image(
                painter = painterResource(Res.drawable.ic_user),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp),
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            UnderlinedText(
                text = user.userName,
            )

            UnderlinedText(
                pointsInfo,
            )
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
