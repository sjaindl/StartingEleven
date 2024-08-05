package com.sjaindl.s11

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform