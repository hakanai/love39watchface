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
    private final float heartWidth;
    private final float heartHeight;

    // Default to interactive for rendering in other places, like the config screen.
    private WatchModeHelper watchMode = new WatchModeHelper(WatchMode.INTERACTIVE);

    HeartLayer(Context context) {
        heartGlowPaint = PaintUtils.createGlowPaint(context, R.color.heart_fill);

        heart = new Heart(context);

        heartWidth = context.getResources().getDimension(R.dimen.analog_heart_width);
        heartHeight = context.getResources().getDimension(R.dimen.analog_heart_height);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        int heartX = bounds.centerX();
        int heartY = bounds.height() * 45 / 64;
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
