package com.bariski.cryptoniffler.data.cache

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.bariski.cryptoniffler.domain.model.CalendarCategory
import com.bariski.cryptoniffler.domain.model.CalendarCoin
import com.bariski.cryptoniffler.domain.model.Coin
import com.bariski.cryptoniffler.domain.model.Exchange


@Dao
interface CNDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCoins(coins: List<Coin>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExchanges(exchange: List<Exchange>)

    @Query("SELECT * FROM coin")
    fun loadAllCoins(): List<Coin>

    @Query("SELECT * FROM exchange")
    fun loadAllExchanges(): List<Exchange>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCalendarCoins(coins: List<CalendarCoin>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCalendarCategories(exchange: List<CalendarCategory>)

    @Query("SELECT * FROM CalendarCoin")
    fun loadAllCalendarCoins(): List<CalendarCoin>

    @Query("SELECT * FROM CalendarCategory")
    fun loadAllCalendarCategories(): List<CalendarCategory>

}