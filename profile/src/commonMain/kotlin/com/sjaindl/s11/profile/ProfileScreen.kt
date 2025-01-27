package com.sjaindl.s11.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.firestore.convertToFirebaseFile
import com.sjaindl.s11.core.theme.HvtdpTheme
import com.sjaindl.s11.photopicker.PhotoPickerScreen
import com.sjaindl.s11.photopicker.files.FileHandler
import com.sjaindl.s11.profile.UserState.Error
import com.sjaindl.s11.profile.UserState.Initial
import com.sjaindl.s11.profile.UserState.Loading
import com.sjaindl.s11.profile.UserState.NoUser
import com.sjaindl.s11.profile.UserState.User
import dev.gitlive.firebase.storage.File
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import startingeleven.profile.generated.resources.Res
import startingeleven.profile.generated.resources.deleteAccount
import startingeleven.profile.generated.resources.deleteAccountConfirm
import startingeleven.profile.generated.resources.deleteAccountDismiss
import startingeleven.profile.generated.resources.deleteAccountText
import startingeleven.profile.generated.resources.deleteAccountTitle
import startingeleven.profile.generated.resources.email
import startingeleven.profile.generated.resources.noMail
import startingeleven.profile.generated.resources.notSignedIn
import startingeleven.profile.generated.resources.onProfilePictureDeleted
import startingeleven.profile.generated.resources.onProfilePictureError
import startingeleven.profile.generated.resources.onProfilePictureUpdated
import startingeleven.profile.generated.resources.onUserNameChanged
import startingeleven.profile.generated.resources.profileName

@Composable
fun ProfileScreen(
    userState: UserState,
    changeUserName: (String, String) -> Unit,
    uploadProfilePhoto: (String, File) -> Unit,
    deleteProfilePhoto: (String) -> Unit,
    loadUser: () -> Unit,
    onDeleteAccount: () -> Unit,
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val coroutineScope = rememberCoroutineScope()

    val fileHandler = remember {
        FileHandler()
    }

    val profilePictureErrorText = stringResource(Res.string.onProfilePictureError)
    val profilePictureUpdatedText = stringResource(Res.string.onProfilePictureUpdated)
    val profilePictureDeletedText = stringResource(Res.string.onProfilePictureDeleted)
    val userNameChangedText = stringResource(Res.string.onUserNameChanged)

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
    ) { paddingValues ->
        when (val state = userState) {
            Initial, Loading -> {
                LoadingScreen()
            }

            is Error -> {
                ErrorScreen(
                    text = state.message,
                    onButtonClick = loadUser,
                )
            }

            NoUser -> {
                ErrorScreen(
                    text = stringResource(resource = Res.string.notSignedIn),
                )
            }

            is User -> {
                ProfileScreenContent(
                    userName = state.name,
                    email = state.email,
                    profileImageUri = state.photoUrl,
                    profilePhotoRefImageUri = state.photoRefDownloadUrl,
                    profilePhotoRefTimestamp = state.profilePhotoRefTimestamp,
                    onImagePicked = {
                        val filePath = fileHandler.getTemporaryFilePath(it)
                        if (filePath == null) {
                            coroutineScope.launch {
                                snackBarHostState.showSnackbar(message = profilePictureErrorText)
                            }
                        } else {
                            val file = convertToFirebaseFile(filePath = filePath)
                            uploadProfilePhoto(state.uid, file)

                            coroutineScope.launch {
                                snackBarHostState.showSnackbar(message = profilePictureUpdatedText)
                            }
                        }
                    },
                    onUserNameChanged = { newName ->
                        changeUserName(state.uid, newName)

                        coroutineScope.launch {
                            snackBarHostState.showSnackbar(message = userNameChangedText)
                        }
                    },
                    onDeleteProfileImage = {
                        deleteProfilePhoto(state.uid)

                        coroutineScope.launch {
                            snackBarHostState.showSnackbar(message = profilePictureDeletedText)
                        }
                    },
                    onDeleteAccount = onDeleteAccount,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = paddingValues),
                )
            }
        }
    }
}

@Composable
private fun ProfileScreenContent(
    userName: String?,
    email: String?,
    profileImageUri: String?,
    profilePhotoRefImageUri: String?,
    profilePhotoRefTimestamp: String?,
    onImagePicked: (ByteArray) -> Unit,
    onUserNameChanged: (String) -> Unit,
    onDeleteProfileImage: () -> Unit,
    onDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPhotoPickerBottomScreen by remember {
        mutableStateOf(value = false)
    }

    var showDeleteAccountConfirmationDialog by remember {
        mutableStateOf(value = false)
    }

    var displayName by remember {
        mutableStateOf(value = userName.orEmpty())
    }

    var userNameEditMode by remember {
        mutableStateOf(value = false)
    }

    Column(
        modifier = modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Avatar(
            profileImageUri = profileImageUri,
            profilePhotoRefImageUri = profilePhotoRefImageUri,
            profilePhotoRefTimestamp = profilePhotoRefTimestamp,
            onAddButtonClicked = {
                showPhotoPickerBottomScreen = true
            },
        )

        TextField(
            value = displayName,
            onValueChange = {
                displayName = it
            },
            enabled = userNameEditMode,
            label = {
                Text(text = stringResource(resource = Res.string.profileName))
            },
            trailingIcon = {
                if (userNameEditMode) {
                    Image(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                userNameEditMode = !userNameEditMode
                                onUserNameChanged(displayName)
                            }
                    )
                } else {
                    Image(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                userNameEditMode = !userNameEditMode
                            }
                    )
                }
            },
        )

        TextField(
            value = email ?: stringResource(Res.string.noMail),
            onValueChange = {
                // email can't be changed
            },
            label = {
                Text(text = stringResource(Res.string.email))
            },
            enabled = false,
        )

        OutlinedButton(
            onClick = {
                showDeleteAccountConfirmationDialog = true
            },
        ) {
            Text(text = stringResource(resource = Res.string.deleteAccount))
        }
    }

    if (showPhotoPickerBottomScreen) {
        PhotoPickerScreen(
            onByteArray = {
                it?.let {
                    onImagePicked(it)
                }

                showPhotoPickerBottomScreen = false
            },
            onDeletePhoto = {
                onDeleteProfileImage()
                showPhotoPickerBottomScreen = false
            },
        )
    }

    if (showDeleteAccountConfirmationDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteAccountConfirmationDialog = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteAccountConfirmationDialog = false
                        onDeleteAccount()
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.deleteAccountConfirm),
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDeleteAccountConfirmationDialog = false
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.deleteAccountDismiss),
                    )
                }
            },
            title = {
                Text(text = stringResource(Res.string.deleteAccountTitle))
            },
            text = {
                Text(text = stringResource(Res.string.deleteAccountText))
            },
        )
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    HvtdpTheme {
        ProfileScreen(
            userState = User(
                uid = "123",
                name = "Daniel Fabian",
                email = "df@hvtdp.at",
                photoUrl = "dummy",
                photoRefDownloadUrl = null,
                profilePhotoRefTimestamp = null,
            ),
            changeUserName = { _, _ -> },
            uploadProfilePhoto = { _, _ -> },
            deleteProfilePhoto = { _ -> },
            loadUser = { },
            onDeleteAccount = { },
        )
    }
}
