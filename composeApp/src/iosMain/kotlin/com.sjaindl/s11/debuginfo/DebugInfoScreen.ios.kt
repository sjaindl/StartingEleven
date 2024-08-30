package com.sjaindl.s11.debuginfo

import platform.Foundation.NSBundle

actual class AppVersionInfo actual constructor() {
    actual fun getVersionCode(): Int {
        val versionString = NSBundle.mainBundle.infoDictionary?.get("CFBundleVersion") as? String ?: "-1"
        return versionString.toIntOrNull() ?: -1
    }

    actual fun getVersionName(): String {
        return NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String ?: "Unknown"
    }
}
