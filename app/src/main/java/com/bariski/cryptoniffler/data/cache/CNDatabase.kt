package com.bariski.cryptoniffler.data.cache

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.bariski.cryptoniffler.domain.model.CalendarCategory
import com.bariski.cryptoniffler.domain.model.CalendarCoin

import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.domain.model.Exchange

@Database(entities = [Exchange::class, Coin::class, CalendarCoin::class, CalendarCategory::class], version = 1)
abstract class CNDatabase : RoomDatabase() {
    abstract fun dao(): CNDao
}