package com.bariski.cryptoniffler.data.logging

import com.bariski.cryptoniffler.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class GlobalHeaderInterceptor: HttpLoggingInterceptor() {


    override fun intercept(chain: Interceptor.Chain?): Response {
        val builder = chain!!.request()!!.newBuilder()
        builder.addHeader("version",BuildConfig.VERSION_CODE.toString())
        builder.addHeader("client","android")
        builder.addHeader("locale", Locale.getDefault().language)
        return super.intercept(chain, builder.build())
    }


}
