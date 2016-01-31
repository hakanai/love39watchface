package org.trypticon.android.love39watchface;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.Gravity;
import android.view.WindowInsets;

import com.ustwo.clockwise.WatchFaceTime;
import com.ustwo.clockwise.WatchMode;

import org.trypticon.android.love39watchface.config.ConfigKeys;
import org.trypticon.android.love39watchface.framework.ConfigurableWatchFace;
import org.trypticon.android.love39watchface.framework.WatchModeAware;
import org.trypticon.android.love39watchface.framework.WatchModeHelper;
import org.trypticon.android.love39watchface.framework.WatchShape;
import org.trypticon.android.love39watchface.layers.Layer;
import org.trypticon.android.love39watchface.layers.LayerFactory;
import org.trypticon.android.love39watchface.time.MultiTime;

/**
 * The main watch face.
 */
public class Love39WatchFace extends ConfigurableWatchFace implements WatchModeAware {
    // I originally determined 20 experimentally.
    // The ustwo examples use 33, so that might be better.
    // The correct value probably depends on screen resolution!
    private static final long INTERACTIVE_UPDATE_RATE_MS = 33;

    private WindowInsets lastScreenInsets;
    private Layer layer;

    private MultiTime time;
    private TimeStyle timeStyle;
    private DateStyle dateStyle;

    @Override
    public void onCreate() {
        super.onCreate();

        time = new MultiTime();
        time.setTo(getTime());

        updateConfiguration(PreferenceManager.getDefaultSharedPreferences(this));
    }

    @Override
    protected WatchFaceStyle getWatchFaceStyle() {
        return new WatchFaceStyle.Builder(this)
                .setStatusBarGravity(Gravity.CENTER)
                .build();
    }

    @Override
    protected long getInteractiveModeUpdateRate() {
        return INTERACTIVE_UPDATE_RATE_MS;
    }

    @Override
    protected void onLayout(com.ustwo.clockwise.WatchShape shape, Rect screenBounds, WindowInsets screenInsets) {
        super.onLayout(shape, screenBounds, screenInsets);

        // null on update configuration on startup, delay creation until we get layout info.
        if (screenInsets != null) {
            lastScreenInsets = screenInsets;

            layer = LayerFactory.createWatchFaceLayers(this, timeStyle, dateStyle);
            layer.updateShape(WatchShape.forScreen(screenBounds, screenInsets));
            layer.setBounds(screenBounds);

            updateWatchMode(new WatchModeHelper(getCurrentWatchMode()));
        }
    }

    @Override
    protected void onWatchModeChanged(WatchMode watchMode) {
        super.onWatchModeChanged(watchMode);
        updateWatchMode(new WatchModeHelper(watchMode));
    }

    @Override
    public void updateWatchMode(WatchModeHelper mode) {
        layer.updateWatchMode(mode);
    }

    @Override
    protected void onTimeChanged(WatchFaceTime oldTime, WatchFaceTime newTime) {
        super.onTimeChanged(oldTime, newTime);

        time.setTo(newTime);
        layer.updateTime(time);
    }

    @Override
    protected void onWatchFaceConfigChanged(SharedPreferences sharedPreferences, String key) {
        updateConfiguration(sharedPreferences);
    }

    private void updateConfiguration(SharedPreferences preferences) {
        if (preferences.getBoolean(ConfigKeys.DOZENAL_TIME_KEY, false)) {
            timeStyle = TimeStyle.DOZENAL;
        } else {
            timeStyle = TimeStyle.CLASSIC;
        }

        if (preferences.getBoolean(ConfigKeys.DOZENAL_CALENDAR_KEY, false)) {
            dateStyle = DateStyle.DOZENAL;
        } else {
            dateStyle = DateStyle.CLASSIC;
        }

        onLayout(getWatchShape(), new Rect(0, 0, getWidth(), getHeight()), lastScreenInsets);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        layer.draw(canvas);
    }
}
