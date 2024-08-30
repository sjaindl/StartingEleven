package com.sjaindl.s11.debuginfo

import android.content.Context
import android.content.pm.PackageManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class AppVersionInfo actual constructor(): KoinComponent  {
    private val context: Context by inject()

    actual fun getVersionCode(): Int {
        val packageManager = context.packageManager
        val packageName = context.packageName
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val versionCode = packageInfo.longVersionCode.toInt()
            return versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            return -1
        }
    }

    actual fun getVersionName(): String {
        val packageManager = context.packageManager
        val packageName = context.packageName
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val versionName = packageInfo.versionName
            return versionName
        } catch (e: PackageManager.NameNotFoundException) {
            return "Unknown"
        }
    }
}
