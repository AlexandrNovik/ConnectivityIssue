package com.dev.aliaksandr.connectivityissue.domain.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import javax.inject.Inject

/**
 * @author Alexander Novik
 */
class ConnectivityStateChangedReceiver
@Inject constructor(private val networkStateObservable: NetworkStateObservable) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        networkStateObservable.onConnectivityStateChanged(ConnectivityState(isConnected(context)))
    }

    private fun isConnected(context: Context): Boolean {
        val connectivityService = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityService.activeNetworkInfo?.isConnected ?: false
    }
}