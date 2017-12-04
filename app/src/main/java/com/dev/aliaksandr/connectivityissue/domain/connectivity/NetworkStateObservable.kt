package com.dev.aliaksandr.connectivityissue.domain.connectivity

import rx.Observable
import rx.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Alexander Novik
 */
@Singleton
class NetworkStateObservable @Inject constructor() {
    private val connectivitySubject = BehaviorSubject.create<ConnectivityState>(ConnectivityState(false))

    fun onConnectivityStateChanged(state: ConnectivityState) {
        connectivitySubject.onNext(state)
    }

    fun observeConnectivityState(): Observable<ConnectivityState> = connectivitySubject.asObservable()

    fun isConnected(): Boolean = connectivitySubject.value.isConnected
}