package com.bariski.cryptoniffler.presentation.injection

import com.bariski.cryptoniffler.presentation.injection.scopes.PerActivity
import com.bariski.cryptoniffler.presentation.main.CoinDetailActivity
import com.bariski.cryptoniffler.presentation.main.MainActivity
import com.bariski.cryptoniffler.presentation.main.SearchActivity
import com.bariski.cryptoniffler.presentation.main.inject.CoinDetailModule
import com.bariski.cryptoniffler.presentation.main.inject.MainModule
import com.bariski.cryptoniffler.presentation.main.inject.SearchModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(MainModule::class))
    abstract fun mainActivity(): MainActivity


    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(CoinDetailModule::class))
    abstract fun coindetailActivity(): CoinDetailActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(SearchModule::class))
    abstract fun searchActivity(): SearchActivity

}