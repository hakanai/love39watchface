package org.trypticon.dozenalwatchface;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Helper to hold a paint along with the current paint based on the watch mode.
 */
public abstract class PaintHolder {
    private final Paint originalPaint = new Paint();
    private final Paint modifiedPaint = new Paint();
    private final boolean usedForLargeFills;

    PaintHolder(boolean usedForLargeFills) {
        this.usedForLargeFills = usedForLargeFills;
        configure(originalPaint);
    }

    protected abstract void configure(Paint paint);

    protected void configureForPaintingOnBlack(Paint paint) {
    }

    Paint getPaint() {
        return modifiedPaint;
    }

    void updateWatchMode(WatchModeHelper mode) {
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
