package com.dev.aliaksandr.connectivityissue.domain.main

/**
 * @author Alexander Novik
 */
interface MainContract {
    interface View {
        fun updateCounterText(value: String)
        fun updateConnectionText(value: String)
    }

    interface Presenter {
        fun start()
        fun release()
    }
}