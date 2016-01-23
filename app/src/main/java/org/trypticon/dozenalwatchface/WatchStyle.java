package org.trypticon.dozenalwatchface;

/**
 * Abstraction for different watch styles.
 */
abstract class WatchStyle {
    abstract Ticks getTicks();
    abstract Time getTime();
    abstract DateFormat getDateFormat();
}
