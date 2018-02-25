package com.bariski.cryptoniffler.data.factory

import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.presentation.common.models.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class ImageRepositoryImpl : ImageLoader {

    override fun loadImage(request: ImageRequest) {
        val requestOptions = RequestOptions()
        request.placeholder?.let {
            requestOptions.placeholder(it)
        }
        request.errorImage?.let {
            requestOptions.error(it)
        }
        if (request.activity != null && !request.activity.isDestroyed) {

            Glide.with(request.activity)
                    .setDefaultRequestOptions(requestOptions)
                    .load(request.url)
                    .into(request.target)

        } else if (request.fragment != null && !request.fragment.isDetached) {
            Glide.with(request.fragment)
                    .setDefaultRequestOptions(requestOptions)
                    .load(request.url)
                    .into(request.target)
        }
    }
}
