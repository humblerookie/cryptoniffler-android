package com.bariski.cryptoniffler.data.factory

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.presentation.common.models.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


class ImageRepositoryImpl(val context: Context) : ImageLoader {

    override fun loadImage(request: ImageRequest) {
        val requestOptions = RequestOptions()
        request.placeholder?.let {
            requestOptions.placeholder(it)
        }
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        } else {
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
        }
        request.errorImage?.let {
            requestOptions.error(it)
        }

        if (request.activity != null && !request.activity.isDestroyed) {

            var builder: RequestBuilder<Drawable> = Glide.with(request.activity)
                    .setDefaultRequestOptions(requestOptions)
                    .load(request.url)
            if (request.makeCircular) {
                builder = builder.apply(RequestOptions.circleCropTransform())
            }
            builder.into(request.target)


        } else if (request.fragment != null && !request.fragment.isDetached) {
            var builder: RequestBuilder<Drawable> = Glide.with(request.fragment)
                    .setDefaultRequestOptions(requestOptions)
                    .load(request.url)
            if (request.makeCircular) {
                builder = builder.apply(RequestOptions.circleCropTransform())
            }
            builder.into(request.target)

        }
    }
}
