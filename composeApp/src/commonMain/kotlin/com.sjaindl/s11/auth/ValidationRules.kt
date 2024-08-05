package com.sjaindl.s11.auth

object ValidationRules {
    const val EMAIL_REGEX = "\\A[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*" +
            "@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\z"
}

fun String.isValidMail(): Boolean {
    return matches(regex = Regex(pattern = ValidationRules.EMAIL_REGEX))
}

fun String.isValidName(): Boolean {
    return isNotEmpty()
}

fun String.isValidPassword(): Boolean {
    return length >= 6
}
