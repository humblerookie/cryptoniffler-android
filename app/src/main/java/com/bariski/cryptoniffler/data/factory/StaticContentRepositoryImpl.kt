package com.bariski.cryptoniffler.data.factory

import com.bariski.cryptoniffler.domain.model.Info
import com.bariski.cryptoniffler.domain.repository.StaticContentRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.Single

open class StaticContentRepositoryImpl(val remoteConfig: FirebaseRemoteConfig) : StaticContentRepository {

    private val KEY_EXCHANGE_INFO = "key_exchange_info"
    private val KEY_UPCOMING_FEATURES = "key_upcoming_features"

    override fun getStaticInfo(): Single<Info> {
        return Single.just(Info(remoteConfig.getString(KEY_EXCHANGE_INFO), remoteConfig.getString(KEY_UPCOMING_FEATURES)))
    }
}