package com.sjaindl.s11.ai.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import startingeleven.ai.generated.resources.Res
import startingeleven.ai.generated.resources.chatbot

@Composable
fun ChatLoadingScreen() {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(Res.drawable.chatbot),
            contentDescription = "",
            modifier = Modifier
                .size(size = 40.dp)
                .clip(CircleShape)
        )

        Spacer(
            modifier = Modifier
                .size(size = 8.dp)
        )

        JumpingDotsIndicator()
    }
}
