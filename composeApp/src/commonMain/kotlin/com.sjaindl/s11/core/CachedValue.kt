package com.sjaindl.s11.core

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

data class CachedValue<T>(
    var value: T? = null,
    var expiresAt: Long = Clock.System.now().plus(value = 24, unit = DateTimeUnit.HOUR).toEpochMilliseconds(),
) {
    fun get(): T? {
        val now = Clock.System.now().toEpochMilliseconds()
        clearIfExpired(nowInMillis = now)
        return value
    }

    fun clear() {
        value = null
        expiresAt = 0L
    }

    private fun isExpired(nowInMillis: Long): Boolean = expiresAt < nowInMillis

    private fun clearIfExpired(nowInMillis: Long) {
        if (value != null && isExpired(nowInMillis)) {
            clear()
        }
    }
}
