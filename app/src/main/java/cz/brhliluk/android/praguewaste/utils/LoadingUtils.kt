package cz.brhliluk.android.praguewaste.utils

import androidx.compose.runtime.MutableState

inline fun <T> MutableState<Boolean>.load(
    block: () -> T
): T? {
    value = true
    try {
        return block()
    } finally {
        value = false
    }
}