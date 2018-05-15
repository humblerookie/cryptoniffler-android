package com.bariski.cryptoniffler.data.factory

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Environment
import android.support.v4.content.ContextCompat
import com.bariski.cryptoniffler.BuildConfig
import com.bariski.cryptoniffler.R
import com.bariski.cryptoniffler.data.utils.CircleTransform
import com.bariski.cryptoniffler.domain.repository.ImageLoader
import com.bariski.cryptoniffler.presentation.common.models.ImageRequest
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.*
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream


class ImageRepositoryImpl(val context: Context) : ImageLoader {

    val TAG = "ImageRepository"
    val cache: Cache

    val IMG_SCREENSHOT = "Cryptoniffler_screenshot.png"

    init {
        val builder = Picasso.Builder(context)
        cache = LruCache(context)
        builder.memoryCache(LruCache(context))
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

    override fun saveScreenshot(bitmap: Bitmap): File? {
        val dirPath = Environment.getExternalStorageDirectory().absolutePath + "/Screenshots"
        val dir = File(dirPath)
        if (!dir.exists())
            dir.mkdirs()
        val file = File(dirPath, IMG_SCREENSHOT)
        try {
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()
            return file
        } catch (e: Exception) {
            Timber.e(e)
        }
        return null
    }


    override fun clearCache() {
        cache.clear()
    }
}
