package org.trypticon.android.love39watchface.layers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.ustwo.clockwise.WatchMode;

import org.trypticon.android.love39watchface.R;
import org.trypticon.android.love39watchface.framework.PaintUtils;
import org.trypticon.android.love39watchface.framework.WatchModeHelper;

/**
 * Layer which draws the heart.
 */
class HeartLayer extends Layer {
    private final Heart heart;
    private final Paint heartGlowPaint;

    // Default to interactive for rendering in other places, like the config screen.
    private WatchModeHelper watchMode = new WatchModeHelper(WatchMode.INTERACTIVE);

    HeartLayer(Context context) {
        heartGlowPaint = PaintUtils.createGlowPaint(context, R.color.heart_fill, 400 * Proportions.HEART_GLOW_WIDTH);

        heart = new Heart(context);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        int width = bounds.width();
        float heartX = width * Proportions.HEART_X;
        float heartY = width * Proportions.HEART_Y;
        float heartWidth = width * Proportions.HEART_WIDTH;
        float heartHeight = width * Proportions.HEART_HEIGHT;

        float glowWidth = width * Proportions.HAND_GLOW_WIDTH;
        heartGlowPaint.setStrokeWidth(glowWidth);

        heart.updateBounds(
                heartX - heartWidth / 2,
                heartY - heartHeight / 2,
                heartX + heartWidth / 2,
                heartY + heartHeight / 2);
    }

    @Override
    public void updateWatchMode(WatchModeHelper mode) {
        watchMode = mode;
    }

    @Override
    public void draw(Canvas canvas) {
        if (watchMode.isInteractive()) {
            canvas.drawPath(heart.getPath(), heartGlowPaint);
            heart.draw(canvas);
        }

    }
}
