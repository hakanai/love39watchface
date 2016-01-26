package org.trypticon.android.love39watchface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import org.trypticon.android.love39watchface.framework.PaintHolder;
import org.trypticon.android.love39watchface.framework.WatchModeAware;
import org.trypticon.android.love39watchface.framework.WatchModeHelper;
import org.trypticon.android.love39watchface.framework.Workarounds;

/**
 * Draws the background.
 */
public class Background extends Drawable implements WatchModeAware {
    private final PaintHolder backgroundPaint;

    public Background(final Context context) {
        backgroundPaint = new PaintHolder(true) {
            @Override
            protected void configure(Paint paint) {
                paint.setColor(Workarounds.getColor(context, R.color.analog_background));
            }
        };

    }

    @Override
    public void updateWatchMode(WatchModeHelper mode) {
        backgroundPaint.updateWatchMode(mode);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint.getPaint());
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
