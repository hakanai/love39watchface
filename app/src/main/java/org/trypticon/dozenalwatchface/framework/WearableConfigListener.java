package org.trypticon.dozenalwatchface.framework;

/**
 * Listener for configuration changes made by a config View.
 */
public interface WearableConfigListener {

    void onConfigCompleted(String key, Object value, boolean finish);
}
