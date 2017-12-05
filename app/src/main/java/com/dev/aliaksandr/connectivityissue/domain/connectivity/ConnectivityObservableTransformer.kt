package com.dev.aliaksandr.connectivityissue.domain.connectivity

import com.dev.aliaksandr.connectivityissue.domain.util.isNetworkError
import rx.Observable
import javax.inject.Inject

/**
 * @author Alexander Novik
 */
class ConnectivityObservableTransformer @Inject constructor() {

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
                }
    }
}