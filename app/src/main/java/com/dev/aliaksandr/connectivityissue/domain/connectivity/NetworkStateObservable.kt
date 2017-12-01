package com.dev.aliaksandr.connectivityissue.domain.connectivity

import rx.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Alexander Novik
 */
@Singleton
class NetworkStateObservable @Inject constructor() {
    private val connectivitySubject = PublishSubject.create<ConnectivityState>()

    fun onConnectivityStateChanged(state: ConnectivityState) {
        connectivitySubject.onNext(state)
    }

    fun observeConnectivityState() = connectivitySubject.asObservable()
}