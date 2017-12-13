package com.dev.aliaksandr.connectivityissue.domain.main

import com.dev.aliaksandr.connectivityissue.domain.connectivity.ConnectivityObservableTransformer
import com.dev.aliaksandr.connectivityissue.domain.connectivity.NetworkStateObservable
import com.dev.aliaksandr.connectivityissue.domain.util.logError
import rx.Observable
import rx.Scheduler
import rx.subjects.PublishSubject
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

    private val emmitSourceSubject = PublishSubject.create<Long>()
    private var subscription: CompositeSubscription = CompositeSubscription()

    override fun start() {
        val emmitSubscription = Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribe(
                        { emmitSourceSubject.onNext(it) },
                        { logError(it) })

        val dataSubscription = Observable.combineLatest(
                emmitSourceSubject.asObservable(),
                networkStateObservable.observeConnectivityState(),
                { index, state -> index to state })
                .doOnNext { if (it.second.isConnected.not()) throw IOException() }
                .compose { connectivityObservableTransformer.call(it) }
                .observeOn(postExecutionThread)
                .subscribe(
                        { view?.updateCounterText(it.first.toString()) },
                        { logError(it) })

        val networkSubscription = networkStateObservable
                .observeConnectivityState()
                .observeOn(postExecutionThread)
                .subscribe(
                        { view?.updateConnectionText(it.toString()) },
                        { logError(it) })

        subscription.add(dataSubscription)
        subscription.add(networkSubscription)
        subscription.add(emmitSubscription)
    }

    override fun release() {
        subscription.unsubscribe()
        view = null
    }
}