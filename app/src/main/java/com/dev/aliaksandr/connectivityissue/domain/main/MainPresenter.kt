package com.dev.aliaksandr.connectivityissue.domain.main

import com.dev.aliaksandr.connectivityissue.domain.connectivity.ConnectivityObservableTransformer
import com.dev.aliaksandr.connectivityissue.domain.connectivity.NetworkStateObservable
import com.dev.aliaksandr.connectivityissue.domain.util.logError
import rx.Observable
import rx.Scheduler
import rx.subscriptions.CompositeSubscription
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author Alexander Novik
 */
class MainPresenter(private var view: MainContract.View?,
                    private var networkStateObservable: NetworkStateObservable,
                    private var connectivityObservableTransformer: ConnectivityObservableTransformer,
                    private var postExecutionThread: Scheduler) : MainContract.Presenter {

    private var subscription: CompositeSubscription = CompositeSubscription()

    override fun start() {
        val dataSubscription = Observable
                .interval(1, TimeUnit.SECONDS)
                .onBackpressureDrop()
                .doOnNext { if (networkStateObservable.isConnected().not()) throw IOException() } // emulate exception
                .compose { connectivityObservableTransformer.call(it) }
                .observeOn(postExecutionThread)
                .subscribe(
                        { view?.updateCounterText(it.toString()) },
                        { logError(it) })

        val networkSubscription = networkStateObservable
                .observeConnectivityState()
                .observeOn(postExecutionThread)
                .subscribe(
                        { view?.updateConnectionText(it.toString()) },
                        { logError(it) })

        subscription.add(dataSubscription)
        subscription.add(networkSubscription)
    }

    override fun release() {
        subscription.unsubscribe()
        view = null
    }
}