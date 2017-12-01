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
class MainActivityComponent(private val presenter: MainContract.Presenter?) : AnkoComponent<MainActivity> {
    private lateinit var counterTextView: TextView
    private lateinit var connectionTextView: TextView

    private var connectionState: String = "UNDEFINED"
    private var connectionText: String = "Internet connection is: $connectionState"

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
            button {
                text = "Start"
                onClick {
                    presenter?.start()
                    hide()
                }
            }.lparams(
                    width = matchParent,
                    height = wrapContent,
                    gravity = Gravity.BOTTOM)
        }
    }

    fun updateCounterText(value: String) {
        counterTextView.apply { text = value }
    }

    fun updateConnectionText(value: String) {
        connectionState = value
        connectionTextView.apply { text = connectionState }
    }

    private fun View.hide() {
        this.visibility = View.GONE
    }

    private fun View.show() {
        this.visibility = View.VISIBLE
    }

}