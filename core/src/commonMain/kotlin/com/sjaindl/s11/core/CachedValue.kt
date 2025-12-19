package com.sjaindl.s11.core

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import kotlin.time.Clock.System
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class CachedValue<T> constructor(
    var value: T? = null,
    var expiresAt: Long = System.now().plus(value = 24, unit = DateTimeUnit.HOUR).toEpochMilliseconds(),
) {

    fun get(): T? {
        val now = System.now().toEpochMilliseconds()
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
