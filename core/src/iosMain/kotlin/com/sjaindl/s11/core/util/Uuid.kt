package com.sjaindl.s11.core.util

import platform.Foundation.NSUUID

actual fun generateUUID(): String = NSUUID().UUIDString()
