package com.bariski.cryptoniffler.data.factory

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.bariski.cryptoniffler.BuildConfig
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.data.utils.CircleTransform
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.presentation.common.models.ImageRequest
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator


class ImageRepositoryImpl(val context: Context) : ImageLoader {

    init {
        val builder = Picasso.Builder(context)
        builder.downloader(OkHttp3Downloader(context, Integer.MAX_VALUE.toLong()))
        val built = builder.build()
        // built.setIndicatorsEnabled(BuildConfig.DEBUG)
        built.isLoggingEnabled = BuildConfig.DEBUG
        Picasso.setSingletonInstance(built)
    }

    override fun loadImage(request: ImageRequest) {

        var context: Context? = null
        if (request.activity != null && !request.activity.isDestroyed) {
            context = request.activity
        } else if (request.fragment != null && request.fragment.activity != null && !request.fragment.activity.isDestroyed) {
            context = request.fragment.activity
        }

        if (context != null) {
            var creator = getCreator(request, context)
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                creator = creator.networkPolicy(NetworkPolicy.OFFLINE)
            }
            creator.into(request.target, object : Callback {
                override fun onSuccess() {

                }

                override fun onError() {
                    var creator = getCreator(request, context)
                    creator.into(request.target)
                }
            })
        }
    }

    private fun getCreator(request: ImageRequest, context: Context): RequestCreator {
        var creator = if (request.url.trim().isNotEmpty()) {
            Picasso.with(context).load(request.url)
        } else {
            Picasso.with(context).load(R.drawable.placeholder)
        }
        if (request.makeCircular) {
            creator.transform(CircleTransform())
        }

        request.placeholder?.let {
            creator = creator.placeholder(it)
        }
        request.errorImage?.let {
            creator.error(it)
        }
        return creator
    }
}
