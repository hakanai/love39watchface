package org.trypticon.android.love39watchface.layers;

import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import org.trypticon.android.love39watchface.framework.WatchModeAware;
import org.trypticon.android.love39watchface.framework.WatchModeHelper;
import org.trypticon.android.love39watchface.framework.WatchShape;
import org.trypticon.android.love39watchface.time.MultiTime;

/**
 * Base class for a drawable layer.
 */
public abstract class Layer extends Drawable implements WatchModeAware {
    @Override
    public void updateWatchMode(WatchModeHelper mode) {

    }

    public void updateShape(WatchShape shape) {

    }

    public void updateTime(MultiTime time) {

    }

    @Override
    public void setAlpha(int alpha) {
        // Ignored for now.
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // Ignored for now.
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
