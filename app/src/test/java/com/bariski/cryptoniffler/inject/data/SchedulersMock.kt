package com.bariski.cryptoniffler.inject.data

import com.bariski.cryptoniffler.domain.common.Schedulers
import io.reactivex.Scheduler

class SchedulersMock : Schedulers {
    override fun io(): Scheduler {
        return io.reactivex.schedulers.Schedulers.trampoline()
    }

    override fun ui(): Scheduler {
        return io.reactivex.schedulers.Schedulers.trampoline()
    }

    override fun compute(): Scheduler {
        return io.reactivex.schedulers.Schedulers.trampoline()
    }
}