package com.dev.aliaksandr.connectivityissue.di

import dagger.Module
import dagger.Provides
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Named
import javax.inject.Singleton

/**
 * @author Alexander Novik
 */
@Module
class AppModule {
    @Provides
    @Singleton
    @Named("main_thread")
    fun providePostExecutionThread(): Scheduler = AndroidSchedulers.mainThread()
}
