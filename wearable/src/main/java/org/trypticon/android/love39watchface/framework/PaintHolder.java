/*
 * Copyright © 2016-2017 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 */

package org.trypticon.android.love39watchface.framework;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Helper to hold a paint along with the current paint based on the watch mode.
 */
public abstract class PaintHolder implements WatchModeAware {
    private final Paint originalPaint = new Paint();
    private final Paint modifiedPaint = new Paint();
    private final boolean usedForLargeFills;

    protected PaintHolder(boolean usedForLargeFills) {
        this.usedForLargeFills = usedForLargeFills;
        configure(originalPaint);

        // Initial state is as if we're running interactive, for rendering in places other
        // than the watch face.
        modifiedPaint.set(originalPaint);
    }

    /**
     * Called to initialise the paint object.
     *
     * @param paint the paint object.
     */
    protected abstract void configure(Paint paint);

    /**
     * Override for that rare situation where something you had that was already all black
     * is now being painted on a black background, so you want to change the style to something
     * that will be visible.
     *
     * @param paint the paint to modify.
     */
    protected void configureForPaintingOnBlack(Paint paint) {
    }

    public Paint getPaint() {
        return modifiedPaint;
    }

    @Override
    public void updateWatchMode(WatchModeHelper mode) {
        modifiedPaint.set(originalPaint);
        if (!mode.canAntiAlias()) {
            modifiedPaint.setAntiAlias(false);
        }
        if (!mode.canUseColour()) {
            if (mode.canUseGreyscale()) {
                float[] hsv = new float[3];
                int rgba = originalPaint.getColor();
                Color.colorToHSV(rgba, hsv);
                hsv[1] = 0;
                modifiedPaint.setColor(Color.HSVToColor(Color.alpha(rgba), hsv));
            } else {
                modifiedPaint.setColor(Color.WHITE);
            }
        }
        if (!mode.canDoLargeFills()) {
            configureForPaintingOnBlack(modifiedPaint);
            if (usedForLargeFills) {
                switch (modifiedPaint.getStyle()) {
                    case FILL_AND_STROKE:
                        modifiedPaint.setStyle(Paint.Style.STROKE);
                        break;

                    case FILL:
                        modifiedPaint.setColor(Color.BLACK);
                        break;
                }
            }
        }
    }

}
