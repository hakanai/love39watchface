package org.trypticon.android.love39watchface.framework;

/**
 * Interface to implement on drawable things which support changing rendering based on the
 * mode the watch is running in.
 */
public interface WatchModeAware {

    /**
     * Updates the watch mode.
     *
     * @param mode the new mode.
     */
    void updateWatchMode(WatchModeHelper mode);
}
