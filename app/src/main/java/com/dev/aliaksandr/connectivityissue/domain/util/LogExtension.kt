package com.dev.aliaksandr.connectivityissue.domain.util

import android.util.Log

/**
 * @author Alexander Novik
 */
fun Any.logError(t: Throwable) {
    Log.e(this.javaClass.name, t.message, t)
}