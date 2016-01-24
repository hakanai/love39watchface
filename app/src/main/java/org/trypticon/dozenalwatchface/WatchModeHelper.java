package org.trypticon.dozenalwatchface;

import com.ustwo.clockwise.WatchMode;

/**
 * Helper with additional methods which I think should have just been on {@link WatchMode}.
 */
public class WatchModeHelper {
    private final WatchMode mode;

    public WatchModeHelper(WatchMode mode) {
        this.mode = mode;
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
