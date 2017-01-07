/*
 * Copyright © 2016-2017 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 */

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
