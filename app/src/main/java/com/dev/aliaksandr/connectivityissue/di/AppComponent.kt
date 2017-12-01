package com.dev.aliaksandr.connectivityissue.di

import com.dev.aliaksandr.connectivityissue.ConnectivityIssueApp
import com.dev.aliaksandr.connectivityissue.domain.main.MainPresenter
import dagger.Component
import javax.inject.Singleton

/**
 * @author Alexander Novik
 */
@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {
    fun inject(app: ConnectivityIssueApp)
    fun inject(presenter: MainPresenter)
}