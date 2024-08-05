package com.sjaindl.s11.core

import androidx.compose.runtime.Composable
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.navigationController

@Composable
actual fun BackHandler(isEnabled: Boolean, onBack: () -> Unit) {
    if (!isEnabled) {
        val currentViewController = findCurrentViewController()
        val navigationController = currentViewController?.navigationController

        navigationController?.popViewControllerAnimated(animated = true)

        onBack()
    }
}

private fun findCurrentViewController(): UIViewController? {
    val keyWindow = UIApplication.sharedApplication.keyWindow
    return keyWindow?.rootViewController
}
