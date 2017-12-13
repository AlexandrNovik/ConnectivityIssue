package com.dev.aliaksandr.connectivityissue

import com.dev.aliaksandr.connectivityissue.domain.connectivity.ConnectivityObservableTransformer
import com.dev.aliaksandr.connectivityissue.domain.connectivity.ConnectivityState
import com.dev.aliaksandr.connectivityissue.domain.connectivity.NetworkStateObservable
import com.dev.aliaksandr.connectivityissue.domain.main.MainContract
import com.dev.aliaksandr.connectivityissue.domain.main.MainPresenter
import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainPresenterUnitTest {
    private val testCounterSubject: PublishSubject<String> = PublishSubject.create<String>()
    private val view = object : MainContract.View {
        override fun updateCounterText(value: String) {
            testCounterSubject.onNext(value)
        }

        override fun updateConnectionText(value: String) {
            // not tested
        }
    }

    @Test
    fun connect_available() {
        val networkStateObservable = NetworkStateObservable()
        networkStateObservable.onConnectivityStateChanged(ConnectivityState(true))
        val connectivityObservableTransformer = ConnectivityObservableTransformer(networkStateObservable)

        val postExecutionThread = Schedulers.immediate()

        val presenter = MainPresenter(
                view,
                networkStateObservable,
                connectivityObservableTransformer,
                postExecutionThread)

        val testSubscriber = TestSubscriber<Unit>()
        Observable
                .fromCallable {
                    presenter.start()
                }
                .subscribe(testSubscriber)

        testSubscriber.assertTerminalEvent()
        testSubscriber.assertNoErrors()

        val testCounterSubscriber = TestSubscriber<String>()
        testCounterSubject.asObservable()
                .subscribe(testCounterSubscriber)
        testCounterSubscriber.awaitValueCount(1, 1500, TimeUnit.MILLISECONDS)
        testCounterSubscriber.assertValue("0")
        testCounterSubscriber.assertNoErrors()
        presenter.release()
    }

    @Test
    fun connect_unavailable() {
        val networkStateObservable = NetworkStateObservable()
        networkStateObservable.onConnectivityStateChanged(ConnectivityState(false))
        val connectivityObservableTransformer = ConnectivityObservableTransformer(networkStateObservable)
        val postExecutionThread = Schedulers.immediate()

        val presenter = MainPresenter(
                view,
                networkStateObservable,
                connectivityObservableTransformer,
                postExecutionThread)

        val testSubscriber = TestSubscriber<Unit>()
        Observable
                .fromCallable {
                    presenter.start()
                }
                .subscribe(testSubscriber)

        testSubscriber.assertTerminalEvent()
        testSubscriber.assertNoErrors()

        val testCounterSubscriber = TestSubscriber<String>()
        testCounterSubject.asObservable().subscribe(testCounterSubject)
        testCounterSubscriber.awaitValueCount(1, 1500, TimeUnit.MILLISECONDS)
        testCounterSubscriber.assertNoValues()
        testCounterSubscriber.assertNoErrors()
        presenter.release()
    }

    @Test
    fun switch_internet() {
        val networkStateObservable = NetworkStateObservable()
        networkStateObservable.onConnectivityStateChanged(ConnectivityState(true))
        val connectivityObservableTransformer = ConnectivityObservableTransformer(networkStateObservable)
        val postExecutionThread = Schedulers.immediate()

        val presenter = MainPresenter(
                view,
                networkStateObservable,
                connectivityObservableTransformer,
                postExecutionThread)

        val testSubscriber = TestSubscriber<Unit>()
        Observable
                .fromCallable {
                    presenter.start()
                }
                .subscribe(testSubscriber)

        testSubscriber.assertTerminalEvent()
        testSubscriber.assertNoErrors()

        val testCounterSubscriber = TestSubscriber<String>()
        testCounterSubject.asObservable()
                .subscribe(testCounterSubscriber)
        testCounterSubscriber.awaitValueCount(1, 1500, TimeUnit.MILLISECONDS)
        testCounterSubscriber.assertValue("0")
        testCounterSubscriber.assertNoErrors()
        val testSubscriberSwitch = TestSubscriber<Unit>()
        Observable
                .fromCallable {
                    networkStateObservable.onConnectivityStateChanged(ConnectivityState(false))
                }
                .subscribe(testSubscriberSwitch)

        testSubscriberSwitch.assertTerminalEvent()
        testSubscriberSwitch.assertNoErrors()

        val testCounterSwitchedSubscriber = TestSubscriber<String>()
        testCounterSubject.asObservable()
                .subscribe(testCounterSwitchedSubscriber)
        testCounterSwitchedSubscriber.awaitValueCount(1, 1, TimeUnit.SECONDS)
        testCounterSwitchedSubscriber.assertNoValues()
        testCounterSwitchedSubscriber.assertNoErrors()
        presenter.release()
    }
}
