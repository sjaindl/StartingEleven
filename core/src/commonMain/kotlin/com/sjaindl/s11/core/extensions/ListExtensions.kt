package com.sjaindl.s11.core.extensions

fun <T: Any> List<T>.insertAt(index: Int, element: T): List<T> {
    return toMutableList().apply {
        if (size > index) {
            removeAt(index = index)
            add(index = index, element = element)
        }
        add(element = element)
    }
}
