package com.dev.aliaksandr.connectivityissue.domain.connectivity

import com.dev.aliaksandr.connectivityissue.domain.util.isNetworkError
import rx.Observable
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Alexander Novik
 */
class ConnectivityObservableTransformer @Inject constructor() {

    private val repeatTimeout = 2L // seconds

    fun <T : Any> call(observable: Observable<T>): Observable<T> {

        return observable
                .retryWhen { errorObservable ->
                    errorObservable
                            .switchMap { error ->
                                if (error.isNetworkError()) {
                                    errorObservable
                                } else {
                                    Observable.error(error)
                                }
                            }
                            .zipWith(Observable.interval(repeatTimeout, TimeUnit.SECONDS), { _, i -> i })
                            .flatMap { Observable.timer(it.toLong(), TimeUnit.SECONDS) }
                            .observeOn(Schedulers.io())
                }
    }
}