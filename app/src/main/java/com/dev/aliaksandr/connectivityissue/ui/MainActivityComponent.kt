package com.dev.aliaksandr.connectivityissue.ui

import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.dev.aliaksandr.connectivityissue.domain.main.MainContract
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author Alexander Novik
 */
class MainActivityComponent : AnkoComponent<MainActivity> {
    private lateinit var counterTextView: TextView
    private lateinit var connectionTextView: TextView

    private var connectionText: String = "Internet connection is: UNDEFINED"

    override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {
        frameLayout {
            connectionTextView = textView {
                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                text = connectionText
            }
            counterTextView = textView {
                gravity = Gravity.CENTER
                text = "Not started"
            }
        }
    }

    fun updateCounterText(value: String) {
        counterTextView.apply { text = value }
    }

    fun updateConnectionText(value: String) {
        connectionTextView.apply { text = value }
    }

}