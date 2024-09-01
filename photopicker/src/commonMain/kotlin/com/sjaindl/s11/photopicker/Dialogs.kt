package com.sjaindl.s11.photopicker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AlertMessageDialog(
    title: String,
    message: String? = null,
    resource: String? = "ic_error_dialog.xml",
    positiveButtonText: String? = null,
    negativeButtonText: String? = null,
    onPositiveClick: () -> Unit = { },
    onNegativeClick: () -> Unit = { },
) {

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 12.dp),
        ) {
            Column(
                modifier = Modifier.background(colorScheme.secondaryContainer)
                    .padding(all = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                resource?.let {
                    Image(
                        modifier = Modifier.size(100.dp),
                        imageVector = Icons.Default.Camera,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(10.dp))

                message?.let {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Medium,
                        color = colorScheme.onBackground.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp, start = 16.dp),
                ) {
                    negativeButtonText?.let {
                        Button(
                            modifier = Modifier
                                .weight(1f),
                            onClick = {
                                onNegativeClick()
                            },
                            colors = ButtonDefaults.buttonColors().copy(
                                contentColor = Color.White,
                                containerColor = colorScheme.primary,
                            ),
                        ) {
                            Text(
                                text = it,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    positiveButtonText?.let {
                        Button(
                            modifier = Modifier.weight(weight = 1f),
                            onClick = {
                                onPositiveClick()
                            },
                            colors = ButtonDefaults.buttonColors().copy(
                                contentColor = Color.White,
                                containerColor = colorScheme.primary,
                            )
                        ) {
                            Text(
                                text = it,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
        }
    }
}
