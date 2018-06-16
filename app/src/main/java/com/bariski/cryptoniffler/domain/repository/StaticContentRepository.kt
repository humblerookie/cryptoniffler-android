package com.bariski.cryptoniffler.domain.repository

import com.bariski.cryptoniffler.domain.model.Info
import io.reactivex.Single

interface StaticContentRepository {

    fun getStaticInfo(): Single<Info>
}