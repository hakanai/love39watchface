/*
 * Copyright © 2016-2017 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 */

package org.trypticon.android.love39watchface.framework;

import com.ustwo.clockwise.WatchMode;

/**
 * Helper with additional methods which I think should have just been on {@link WatchMode}.
 */
public class WatchModeHelper {
    private final WatchMode mode;

    public WatchModeHelper(WatchMode mode) {
        this.mode = mode;
    }

    public boolean isInteractive() {
        return mode == WatchMode.INTERACTIVE;
    }

    boolean canUseColour() {
        switch (mode) {
            case INTERACTIVE:
                return true;
            case AMBIENT:
            case LOW_BIT:
            case BURN_IN:
            case LOW_BIT_BURN_IN:
                return false;
            default:
                throw new IllegalStateException("Missing case: " + mode);
        }
    }

    boolean canUseGreyscale() {
        switch (mode) {
            case INTERACTIVE:
            case AMBIENT:
            case BURN_IN:
                return true;
            case LOW_BIT:
            case LOW_BIT_BURN_IN:
                return false;
            default:
                throw new IllegalStateException("Missing case: " + mode);
        }
    }

    boolean canAntiAlias() {
        switch (mode) {
            case INTERACTIVE:
            case AMBIENT:
            case BURN_IN:
                return true;
            case LOW_BIT:
            case LOW_BIT_BURN_IN:
                return false;
            default:
                throw new IllegalStateException("Missing case: " + mode);
        }
    }

    boolean canDoLargeFills() {
        switch (mode) {
            case INTERACTIVE:
            case AMBIENT:
            case LOW_BIT:
                return true;
            case BURN_IN:
            case LOW_BIT_BURN_IN:
                return false;
            default:
                throw new IllegalStateException("Missing case: " + mode);
        }
    }
}
