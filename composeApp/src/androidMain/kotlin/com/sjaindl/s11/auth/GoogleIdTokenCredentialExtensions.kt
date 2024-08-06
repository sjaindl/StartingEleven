package com.sjaindl.s11.auth

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.sjaindl.s11.auth.model.GoogleAccount
import com.sjaindl.s11.auth.model.Profile

val GoogleIdTokenCredential.googleAccount: GoogleAccount
    get() = GoogleAccount(
        idToken = idToken,
        profile = Profile(
            name = displayName,
            familyName = familyName,
            givenName = givenName,
            email = id,
            pictureUrl = profilePictureUri.toString(),
        ),
    )
