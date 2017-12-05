package com.dev.aliaksandr.connectivityissue.di

import com.dev.aliaksandr.connectivityissue.ConnectivityIssueApp
import com.dev.aliaksandr.connectivityissue.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * @author Alexander Novik
 */
@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {
    fun inject(app: ConnectivityIssueApp)
    fun inject(activity: MainActivity)
}