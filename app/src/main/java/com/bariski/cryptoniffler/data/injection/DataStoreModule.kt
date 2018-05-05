package com.bariski.cryptoniffler.data.injection

import android.content.Context
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
}