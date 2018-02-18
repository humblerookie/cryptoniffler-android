package com.bariski.cryptoniffler.domain.common

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Singleton

@Module
class SchedulerModule {

    @Provides
    @Singleton
    fun provideScheduler(): Schedulers {
        return object : com.bariski.cryptoniffler.domain.common.Schedulers {
            override fun io(): Scheduler {
                return io.reactivex.schedulers.Schedulers.io()
            }

            override fun ui(): Scheduler {
                return AndroidSchedulers.mainThread()
            }

            override fun compute(): Scheduler {
                return io.reactivex.schedulers.Schedulers.computation()
            }

        }
    }
}