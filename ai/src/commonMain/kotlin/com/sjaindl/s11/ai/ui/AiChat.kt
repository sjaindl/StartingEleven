package com.sjaindl.s11.ai.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sjaindl.s11.ai.navigation.navigateToAi
import com.sjaindl.s11.core.theme.HvtdpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AiChat(navController: NavController) {
    FloatingActionButton(
        onClick = {
            navController.navigateToAi()
        }
    ) {
        Icon(imageVector = Icons.Filled.Forum, contentDescription = "Chat")
    }
}

@Preview
@Composable
fun AiChatPreview() {
    val navController = rememberNavController()

    HvtdpTheme {
        AiChat(navController = navController)
    }
}
