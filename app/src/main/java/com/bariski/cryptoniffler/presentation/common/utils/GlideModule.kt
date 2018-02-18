package com.bariski.cryptoniffler.presentation.common.utils

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions


@GlideModule
class GlideModule : AppGlideModule() {
    override fun applyOptions(context: Context?, builder: GlideBuilder) {
        val memoryCacheSizeBytes = 1024 * 1024 * 20
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes))
        val diskCacheSizeBytes = 1024 * 1024 * 100 // 100 MB
        builder.setDiskCache(ExternalCacheDiskCacheFactory(context, diskCacheSizeBytes))
        builder.setDefaultRequestOptions(
                RequestOptions()
                        .format(DecodeFormat.PREFER_RGB_565).diskCacheStrategy(DiskCacheStrategy.NONE)
        )
    }
}