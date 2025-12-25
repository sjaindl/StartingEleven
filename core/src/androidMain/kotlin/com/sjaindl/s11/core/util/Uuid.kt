package com.sjaindl.s11.core.util

import java.util.UUID

actual fun generateUUID(): String = UUID.randomUUID().toString()
