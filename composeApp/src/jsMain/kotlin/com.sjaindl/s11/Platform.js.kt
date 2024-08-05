package com.sjaindl.s11

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform  = JsPlatform()
