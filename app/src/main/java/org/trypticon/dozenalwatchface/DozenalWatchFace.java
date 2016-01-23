package org.trypticon.dozenalwatchface;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.WindowInsets;

import com.ustwo.clockwise.WatchFace;
import com.ustwo.clockwise.WatchShape;

/**
 * The main watch face.
 */
public class DozenalWatchFace extends WatchFace {
    // I originally determined 20 experimentally.
    // The ustwo examples use 33, so that might be better.
    // The correct value probably depends on screen resolution!
    private static final long INTERACTIVE_UPDATE_RATE_MS = 33;

    private DozenalWatchFaceDrawable watchFaceDrawable;

    @Override
    public void onCreate() {
        super.onCreate();

        watchFaceDrawable = new DozenalWatchFaceDrawable(this);
    }

    @Override
    protected void onLayout(WatchShape shape, Rect screenBounds, WindowInsets screenInsets) {
        watchFaceDrawable.updateShape(shape == WatchShape.CIRCLE);
        watchFaceDrawable.setBounds(screenBounds);
        //TODO Insets, for the chin I guess.
    }

    @Override
    protected long getInteractiveModeUpdateRate() {
        return INTERACTIVE_UPDATE_RATE_MS;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        watchFaceDrawable.draw(canvas);
    }
}
