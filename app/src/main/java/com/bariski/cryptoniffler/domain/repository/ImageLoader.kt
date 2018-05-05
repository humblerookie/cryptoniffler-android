package com.bariski.cryptoniffler.domain.repository

import com.bariski.cryptoniffler.presentation.common.models.ImageRequest

interface ImageLoader {

    fun loadImage(request: ImageRequest)
    fun clearCache()
}