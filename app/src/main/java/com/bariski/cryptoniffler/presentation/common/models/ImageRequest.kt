package com.bariski.cryptoniffler.presentation.common.models

import android.app.Activity
import android.app.Fragment
import android.widget.ImageView

data class ImageRequest(val target: ImageView, val placeholder: Int?, val url: String, val fragment: Fragment?, val activity: Activity?, val errorImage: Int?, val makeCircular: Boolean, val radius: Int = 0)