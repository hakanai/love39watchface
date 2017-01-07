/*
 * Copyright © 2016-2017 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 */

package org.trypticon.android.love39watchface.framework;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ustwo.clockwise.ConnectedWatchFace;

/**
 * A watch face with additional configuration capabilities.
 */
public abstract class ConfigurableWatchFace extends ConnectedWatchFace {
    private final SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    onWatchFaceConfigChanged(sharedPreferences, key);
                }
            };

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    protected abstract void onWatchFaceConfigChanged(SharedPreferences sharedPreferences, String key);

}
