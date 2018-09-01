package com.bariski.cryptoniffler.data.injection

import android.content.Context
import com.bariski.cryptoniffler.BuildConfig
import com.bariski.cryptoniffler.data.factory.ImageRepositoryImpl
import com.bariski.cryptoniffler.data.logging.GlobalHeaderInterceptor
import com.bariski.cryptoniffler.data.logging.HttpLoggingInterceptor
import com.bariski.cryptoniffler.data.utils.NetworkUtil
import com.bariski.cryptoniffler.data.utils.NetworkUtilImpl
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.domain.util.BASE_URL
import com.bariski.cryptoniffler.presentation.CryptNifflerApplication
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.*
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module(includes = [AppModule::class])
class NetworkModule {

    @Provides
    @Named(BASE_URL_KEY)
    fun provideBaseUrl(remoteConfig: FirebaseRemoteConfig): String {
        return remoteConfig.getString(BASE_URL)
    }

    @Singleton
    @Provides
    fun provideRetrofit(@Named(BASE_URL_KEY) baseUrl: String, okHttpClient: OkHttpClient, converterFactory: MoshiConverterFactory, callAdapterFactory: CallAdapter.Factory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()

    }

    @Singleton
    @Provides
    fun provideOkHttpClient(requestInterceptor: Interceptor): OkHttpClient {
        val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA)
                .build()
        return OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(requestInterceptor)
                .connectionSpecs(Collections.singletonList(spec))
                .build()

    }

    @Singleton
    @Provides
    fun provideNetworkInterceptor(): Interceptor {
        return GlobalHeaderInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
    }

    @Singleton
    @Provides
    fun provideHttpCache(application: CryptNifflerApplication): Cache {
        val cacheSize = 10 * 1024 * 1024L
        return Cache(application.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideConverter(): MoshiConverterFactory {
        return MoshiConverterFactory.create(Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build())
    }

    @Provides
    @Singleton
    fun provideCallAdapter(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun provideImageLoader(context: Context): ImageLoader {
        return ImageRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideNetworkUtil(context: Context): NetworkUtil {
        return NetworkUtilImpl(context)
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
    }

}