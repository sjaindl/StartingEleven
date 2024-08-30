package com.sjaindl.s11.debuginfo

expect class AppVersionInfo() {
    fun getVersionCode(): Int
    fun getVersionName(): String
}
