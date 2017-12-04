package com.dev.aliaksandr.connectivityissue.domain.main

import android.util.Log
import com.dev.aliaksandr.connectivityissue.ConnectivityIssueApp
import com.dev.aliaksandr.connectivityissue.domain.connectivity.ConnectivityObservableTransformer
import com.dev.aliaksandr.connectivityissue.domain.connectivity.NetworkStateObservable
import rx.Observable
import rx.Scheduler
import rx.subscriptions.CompositeSubscription
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

/**
 * @author Alexander Novik
 */
class MainPresenter(private var view: MainContract.View?) : MainContract.Presenter {
    init {
        ConnectivityIssueApp.appComponent.inject(this)
    }

    @field:[Named("main_thread")]
    @Inject lateinit var postExecutionThread: Scheduler
    @Inject lateinit var connectivityObservableTransformer: ConnectivityObservableTransformer
    @Inject lateinit var networkStateObservable: NetworkStateObservable

    private var subscription: CompositeSubscription? = null

    override fun start() {
        val dataSubscription = Observable
                .interval(1, TimeUnit.SECONDS)
                .doOnNext { if (networkStateObservable.isConnected().not()) throw IOException() } // emulate exception
                .compose { connectivityObservableTransformer.call(it) }
                .onBackpressureDrop()
                .observeOn(postExecutionThread)
                .subscribe({
                    view?.updateCounterText(it.toString())
                }, {
                    Log.e("MainPresenter", "Error getting data : ${it.message}")
                })

        val networkSubscription = networkStateObservable
                .observeConnectivityState()
                .observeOn(postExecutionThread)
                .subscribe({
                    view?.updateConnectionText(it.toString())
                }, {
                    Log.e("MainPresenter", "Error observing network: ${it.message}")
                })

        subscription?.add(dataSubscription)
        subscription?.add(networkSubscription)
    }

    override fun release() {
        subscription?.unsubscribe()
        view = null
    }
}