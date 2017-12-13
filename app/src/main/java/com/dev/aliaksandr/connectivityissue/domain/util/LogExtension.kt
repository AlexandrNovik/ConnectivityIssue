package com.dev.aliaksandr.connectivityissue.domain.util

import android.util.Log

/**
 * @author Alexander Novik
 */
fun Any.logError(t: Throwable) {
    Log.e(this.javaClass.simpleName, t.message, t)
}