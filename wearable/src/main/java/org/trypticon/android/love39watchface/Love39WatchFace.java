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
import org.trypticon.android.love39watchface.config.DateStyle;
import org.trypticon.android.love39watchface.config.TimeStyle;
import org.trypticon.android.love39watchface.framework.ConfigurableWatchFace;
import org.trypticon.android.love39watchface.framework.PreferencesUtils;
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
    // 20 appears to be smoother than ustwo's example of 33.
    // The correct value probably depends on screen resolution!
    private static final long INTERACTIVE_UPDATE_RATE_MS = 20;

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
        // Deliberately omitting call to onTimeChanged because it performs expensive
        // date formatting even if debug is off.
        // https://github.com/ustwo/clockwise/issues/33
        //super.onTimeChanged(oldTime, newTime);

        time.setTo(newTime);
        layer.updateTime(time);
    }

    @Override
    protected void onWatchFaceConfigChanged(SharedPreferences sharedPreferences, String key) {
        updateConfiguration(sharedPreferences);
    }

    private void updateConfiguration(SharedPreferences preferences) {
        timeStyle = PreferencesUtils.getEnum(
                preferences, ConfigKeys.TIME_STYLE_KEY, TimeStyle.class, TimeStyle.CLASSIC);
        dateStyle = PreferencesUtils.getEnum(
                preferences, ConfigKeys.DATE_STYLE_KEY, DateStyle.class, DateStyle.CLASSIC);

        onLayout(getWatchShape(), new Rect(0, 0, getWidth(), getHeight()), lastScreenInsets);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        layer.draw(canvas);
    }
}
