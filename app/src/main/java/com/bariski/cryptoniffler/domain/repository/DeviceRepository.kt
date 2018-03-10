package com.bariski.cryptoniffler.domain.repository

import com.bariski.cryptoniffler.domain.model.Device
import com.bariski.cryptoniffler.domain.model.ResponseWrapper
import io.reactivex.Single


interface DeviceRepository : DeviceDataStore {

    fun registerDevice(device: Device): Single<ResponseWrapper>


}