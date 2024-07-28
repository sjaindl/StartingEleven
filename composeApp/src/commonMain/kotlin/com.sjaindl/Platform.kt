package com.sjaindl

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform