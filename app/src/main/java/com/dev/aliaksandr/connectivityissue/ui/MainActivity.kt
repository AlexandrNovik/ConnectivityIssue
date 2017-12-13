package com.dev.aliaksandr.connectivityissue.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dev.aliaksandr.connectivityissue.ConnectivityIssueApp
import com.dev.aliaksandr.connectivityissue.domain.connectivity.ConnectivityObservableTransformer
import com.dev.aliaksandr.connectivityissue.domain.connectivity.NetworkStateObservable
import com.dev.aliaksandr.connectivityissue.domain.main.MainContract
import com.dev.aliaksandr.connectivityissue.domain.main.MainPresenter
import org.jetbrains.anko.setContentView
import rx.Scheduler
import javax.inject.Inject
import javax.inject.Named

class MainActivity : AppCompatActivity(), MainContract.View {
    init {
        ConnectivityIssueApp.appComponent.inject(this)
    }
    @field:[Named("main_thread")]
    @Inject lateinit var postExecutionThread: Scheduler
    @Inject lateinit var connectivityObservableTransformer: ConnectivityObservableTransformer
    @Inject lateinit var networkStateObservable: NetworkStateObservable

    private lateinit var mainActivityComponent: MainActivityComponent
    private lateinit var presenter: MainContract.Presenter

    override fun updateCounterText(value: String) {
        mainActivityComponent.updateCounterText(value)
    }

    override fun updateConnectionText(value: String) {
        mainActivityComponent.updateConnectionText(value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = MainPresenter(
                this,
                networkStateObservable,
                connectivityObservableTransformer,
                postExecutionThread)
        mainActivityComponent = MainActivityComponent()
        mainActivityComponent.setContentView(this)
        presenter.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.release()
    }
}
