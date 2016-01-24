package org.trypticon.android.love39watchface.framework;

/**
 * Listener for configuration changes made by a config View.
 */
public interface WearableConfigListener {

    void onConfigCompleted(String key, Object value, boolean finish);
}
