package com.bariski.cryptoniffler.data.injection

import android.content.Context
import com.bariski.cryptoniffler.data.api.EventsApi
import com.bariski.cryptoniffler.data.factory.EventsRepositoryImpl
import com.bariski.cryptoniffler.data.storage.KeyValueStore
import com.bariski.cryptoniffler.domain.common.Schedulers
import com.bariski.cryptoniffler.domain.repository.EventsRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class EventModule {

    @Provides
    fun provideEventsApi(retrofit: Retrofit): EventsApi {
        return retrofit.create(EventsApi::class.java)
    }

    @Provides
    fun provideEventsRepository(api: EventsApi, remoteConfig: FirebaseRemoteConfig, keyValue: KeyValueStore, moshi: Moshi, context: Context, schedulers: Schedulers): EventsRepository {
        return EventsRepositoryImpl(api, remoteConfig, keyValue, moshi, context, schedulers)
    }
}