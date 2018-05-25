package com.bariski.cryptoniffler.domain.repository

import com.bariski.cryptoniffler.domain.model.ResponseWrapper
import io.reactivex.Single


interface DeviceRepository : DeviceDataStore {

    fun registerDevice(token: String?, id: String?): Single<ResponseWrapper>

}