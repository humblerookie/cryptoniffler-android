package com.bariski.cryptoniffler.domain.common

import io.reactivex.Scheduler

interface Schedulers {

    fun io(): Scheduler
    fun ui(): Scheduler
    fun compute(): Scheduler
}