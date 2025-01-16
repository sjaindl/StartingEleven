package com.sjaindl.s11.core

import androidx.compose.runtime.compositionLocalOf

sealed class Platform(open val name: String) {
    data class iOS(override val name: String) : Platform(name = name)
    data class Android(override val name: String) : Platform(name = name)
}

expect fun getPlatform(): Platform

val LocalPlatform = compositionLocalOf {
    getPlatform()
}
