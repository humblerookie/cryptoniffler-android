package com.bariski.cryptoniffler.presentation.common.extensions

import android.content.Context
import android.support.v4.content.ContextCompat


fun Context.color(resourceId: Int) = ContextCompat.getColor(this, resourceId)