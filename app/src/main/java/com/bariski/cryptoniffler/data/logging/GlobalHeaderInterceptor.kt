package com.bariski.cryptoniffler.data.logging

import com.bariski.cryptoniffler.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class GlobalHeaderInterceptor: HttpLoggingInterceptor() {


    override fun intercept(chain: Interceptor.Chain?): Response {
        val builder = chain!!.request()!!.newBuilder()
        builder.addHeader("version",BuildConfig.VERSION_CODE.toString())
        builder.addHeader("client","android")
        return super.intercept(chain, builder.build())
    }


}
