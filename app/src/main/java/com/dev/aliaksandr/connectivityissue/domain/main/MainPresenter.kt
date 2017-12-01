package com.dev.aliaksandr.connectivityissue.domain.main

import com.dev.aliaksandr.connectivityissue.ConnectivityIssueApp
import rx.Observable
import rx.Scheduler
import rx.Subscription
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

    private var subscription: Subscription? = null

    override fun start() {
        subscription = Observable
                .interval(1, TimeUnit.SECONDS)
                .observeOn(postExecutionThread)
                .subscribe({
                    view?.updateCounterText(it.toString())
                })
    }

    override fun release() {
        subscription?.unsubscribe()
        view = null
    }
}