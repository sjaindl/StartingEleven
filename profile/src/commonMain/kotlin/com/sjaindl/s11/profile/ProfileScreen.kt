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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sjaindl.s11.core.baseui.ErrorScreen
import com.sjaindl.s11.core.baseui.LoadingScreen
import com.sjaindl.s11.core.baseui.S11AppBar
import com.sjaindl.s11.core.theme.HvtdpTheme
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
import startingeleven.profile.generated.resources.email
import startingeleven.profile.generated.resources.noMail
import startingeleven.profile.generated.resources.notSignedIn
import startingeleven.profile.generated.resources.onProfilePictureDeleted
import startingeleven.profile.generated.resources.onProfilePictureUpdated
import startingeleven.profile.generated.resources.onUserNameChanged
import startingeleven.profile.generated.resources.profile

@Composable
fun ProfileScreen(
    navigateUp: () -> Unit,
) {
    val profileViewModel = viewModel {
        ProfileViewModel()
    }

    val userState by profileViewModel.userState.collectAsState()

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val coroutineScope = rememberCoroutineScope()

    val profilePictureUpdatedText = stringResource(Res.string.onProfilePictureUpdated)
    val profilePictureDeletedText = stringResource(Res.string.onProfilePictureDeleted)
    val userNameChangedText = stringResource(Res.string.onUserNameChanged)

    Scaffold(
        topBar = {
            S11AppBar(
                title = stringResource(resource = Res.string.profile),
                userIsSignedIn = false,
                isTopLevelAppBar = false,
                canNavigateBack = true,
                navigateUp = navigateUp,
            )
        },
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
                    onButtonClick = {
                        profileViewModel.loadUser()
                    },
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
                    onImagePicked = {
                        profileViewModel.uploadProfilePhoto(uid = state.uid, file = it)

                        coroutineScope.launch {
                            snackBarHostState.showSnackbar(message = profilePictureUpdatedText)
                        }
                    },
                    onUserNameChanged = { newName ->
                        profileViewModel.changeUserName(uid = state.uid, newName = newName)

                        coroutineScope.launch {
                            snackBarHostState.showSnackbar(message = userNameChangedText)
                        }
                    },
                    onDeleteProfileImage = {
                        profileViewModel.deleteProfilePhoto(uid = state.uid)

                        coroutineScope.launch {
                            snackBarHostState.showSnackbar(message = profilePictureDeletedText)
                        }
                    },
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
    onImagePicked: (File) -> Unit, // TODO
    onUserNameChanged: (String) -> Unit,
    onDeleteProfileImage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPhotoPickerBottomSheet by rememberSaveable {
        mutableStateOf(value = false)
    }

    var displayName by remember {
        mutableStateOf(value = userName.orEmpty())
    }

    var userNameEditMode by rememberSaveable {
        mutableStateOf(value = false)
    }

    // val photoCaptureRequest by rememberPhotoCapture()
    // val photoPickerRequest by rememberPhotoPicker()

    Column(
        modifier = modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Avatar(imageUri = profileImageUri)

        TextField(
            value = displayName,
            onValueChange = {
                displayName = it
            },
            enabled = userNameEditMode,
            label = {
                Text(text = "Benutzername")
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
    }

    if (showPhotoPickerBottomSheet) {
        PhotoPickerBottomSheet(
            onDismiss = { showPhotoPickerBottomSheet = false },
            onDeleteImage = onDeleteProfileImage,
            //photoCaptureRequest = photoCaptureRequest,
            //photoPickerRequest = photoPickerRequest,
            canDeleteImage = profileImageUri != null
        )
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    HvtdpTheme {
        ProfileScreenContent(
            userName = "Daniel Fabian",
            email = "df@hvtdp.at",
            profileImageUri = "dummy",
            onImagePicked = { },
            onUserNameChanged = { },
            onDeleteProfileImage = { },
        )
    }
}
