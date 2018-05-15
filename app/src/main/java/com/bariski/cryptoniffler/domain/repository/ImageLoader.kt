package com.bariski.cryptoniffler.domain.repository

import android.graphics.Bitmap
import com.bariski.cryptoniffler.presentation.common.models.ImageRequest
import java.io.File

interface ImageLoader {

    fun loadImage(request: ImageRequest)
    fun clearCache()
    fun saveScreenshot(bitmap: Bitmap): File?
}