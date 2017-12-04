package com.dev.aliaksandr.connectivityissue.domain.util

import java.io.IOException

/**
 * @author Alexander Novik
 */
fun Throwable?.isNetworkError(): Boolean = this is IOException