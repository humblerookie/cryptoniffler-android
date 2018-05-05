package com.bariski.cryptoniffler.data.injection

import android.content.Context
import com.bariski.cryptoniffler.data.api.CryptoNifflerApi
import com.bariski.cryptoniffler.data.cache.DataCache
import com.bariski.cryptoniffler.data.factory.NifflerRepositoryImpl
import com.bariski.cryptoniffler.data.storage.KeyValueStore
import com.bariski.cryptoniffler.domain.repository.NifflerRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class CoinModule {

    @Provides
    fun provideCoinRepository(context: Context, api: CryptoNifflerApi, firebaseRemoteConfig: FirebaseRemoteConfig, keyValueStore: KeyValueStore, moshi: Moshi, cache: DataCache): NifflerRepository {
        return NifflerRepositoryImpl(context, api, firebaseRemoteConfig, keyValueStore, moshi, cache)
    }

    @Provides
    fun provideCryptoNifflerApi(retrofit: Retrofit): CryptoNifflerApi {
        return retrofit.create(CryptoNifflerApi::class.java)
    }

}