package com.bariski.cryptoniffler.data.logging

import okhttp3.Interceptor
import okhttp3.Response

class GlobalHeaderInterceptor: HttpLoggingInterceptor() {


    override fun intercept(chain: Interceptor.Chain?): Response {
        val builder = chain!!.request()!!.newBuilder()
        return super.intercept(chain, builder.build())
    }


}
