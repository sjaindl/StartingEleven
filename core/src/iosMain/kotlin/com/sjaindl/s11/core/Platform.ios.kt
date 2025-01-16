package com.sjaindl.s11.core

import platform.UIKit.UIDevice

actual fun getPlatform(): Platform = Platform.iOS(
    name = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion,
)
