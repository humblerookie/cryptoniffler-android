package com.bariski.cryptoniffler.presentation.common;

import android.graphics.Bitmap;

public interface BaseView {
    String getMessage(int resourceId);

    Bitmap getScreenShot();

    String getString(int resourceId, Object... args);
}
