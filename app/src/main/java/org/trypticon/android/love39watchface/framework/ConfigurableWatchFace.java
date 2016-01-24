package org.trypticon.android.love39watchface.framework;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ustwo.clockwise.WatchFace;

/**
 * A watch face with additional configuration capabilities.
 */
public abstract class ConfigurableWatchFace extends WatchFace /* TODO extends ConnectedWatchFace */ {
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
