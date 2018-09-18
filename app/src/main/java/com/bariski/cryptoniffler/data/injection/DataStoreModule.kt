package com.bariski.cryptoniffler.data.injection

import android.arch.persistence.room.Room
import android.content.Context
import com.bariski.cryptoniffler.data.cache.CNDao
import com.bariski.cryptoniffler.data.cache.CNDatabase
import com.bariski.cryptoniffler.data.cache.DataCache
import com.bariski.cryptoniffler.data.cache.MemCache
import com.bariski.cryptoniffler.data.factory.DeviceDataStoreImpl
import com.bariski.cryptoniffler.data.storage.KeyValueStore
import com.bariski.cryptoniffler.data.storage.KeyValueStoreImpl
import com.bariski.cryptoniffler.domain.repository.DeviceDataStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DataStoreModule {

    val DB_NAME = "cn_db"

    @Provides
    @Singleton
    fun provideKeyStore(context: Context): KeyValueStore {
        return KeyValueStoreImpl(context)
    }

    @Provides
    @Singleton
    fun provideCache(context: Context): DataCache {
        return MemCache()
    }


    @Provides
    @Singleton
    fun provideDeviceDataStore(keyValueStore: KeyValueStore): DeviceDataStore {
        return DeviceDataStoreImpl(keyValueStore)
    }

    @Provides
    @Singleton
    fun provideCnDB(context: Context): CNDatabase {
        return Room.databaseBuilder(context, CNDatabase::class.java, DB_NAME).build()
    }

    @Provides
    fun provideCnDao(database: CNDatabase): CNDao {
        return database.dao()
    }
}