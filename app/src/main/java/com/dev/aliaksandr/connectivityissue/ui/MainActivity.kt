package com.dev.aliaksandr.connectivityissue.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dev.aliaksandr.connectivityissue.domain.main.MainContract
import com.dev.aliaksandr.connectivityissue.domain.main.MainPresenter
import org.jetbrains.anko.setContentView

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var mainActivityComponent: MainActivityComponent
    private var presenter: MainContract.Presenter? = null

    override fun updateCounterText(value: String) {
        mainActivityComponent.updateCounterText(value)
    }

    override fun updateConnectionText(value: String) {
        mainActivityComponent.updateConnectionText(value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = MainPresenter(this)
        mainActivityComponent = MainActivityComponent(presenter)
        mainActivityComponent.setContentView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.release()
        presenter = null
    }
}
