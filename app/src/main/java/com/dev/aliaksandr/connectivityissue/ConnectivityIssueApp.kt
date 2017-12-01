package com.dev.aliaksandr.connectivityissue

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.dev.aliaksandr.connectivityissue.di.AppComponent
import com.dev.aliaksandr.connectivityissue.di.DaggerAppComponent
import com.dev.aliaksandr.connectivityissue.domain.connectivity.ConnectivityStateChangedReceiver
import javax.inject.Inject

/**
 * @author Alexander Novik
 */
class ConnectivityIssueApp : Application() {
    @Inject lateinit var connectivityStateChangedReceiver: ConnectivityStateChangedReceiver
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
        appComponent.inject(this)
        registerReceiver(connectivityStateChangedReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }
}