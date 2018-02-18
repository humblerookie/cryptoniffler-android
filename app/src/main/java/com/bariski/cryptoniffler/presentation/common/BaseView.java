package com.bariski.cryptoniffler.presentation.common;

public interface BaseView {
    String getMessage(int resourceId);

    String getString(int resourceId, Object... args);
}
