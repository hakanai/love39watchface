package org.trypticon.android.love39watchface;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.Gravity;
import android.view.WindowInsets;

import com.ustwo.clockwise.WatchFaceTime;
import com.ustwo.clockwise.WatchMode;
import com.ustwo.clockwise.WatchShape;

import org.trypticon.android.love39watchface.config.ConfigKeys;
import org.trypticon.android.love39watchface.framework.ConfigurableWatchFace;
import org.trypticon.android.love39watchface.framework.PaintHolder;
import org.trypticon.android.love39watchface.framework.PaintUtils;
import org.trypticon.android.love39watchface.framework.WatchModeAware;
import org.trypticon.android.love39watchface.framework.WatchModeHelper;
import org.trypticon.android.love39watchface.framework.Workarounds;
import org.trypticon.android.love39watchface.time.Time;

/**
 * The main watch face.
 */
public class Love39WatchFace extends ConfigurableWatchFace implements WatchModeAware {
    // I originally determined 20 experimentally.
    // The ustwo examples use 33, so that might be better.
    // The correct value probably depends on screen resolution!
    private static final long INTERACTIVE_UPDATE_RATE_MS = 33;

    private Background background;

    private Heart heart;
    private Paint heartGlowPaint;


    private PaintHolder datePaint;

    private TimeStyle timeStyle;
    private DateStyle dateStyle;

    @Override
    public void onCreate() {
        super.onCreate();

        background = new Background(this);

        heartGlowPaint = PaintUtils.createGlowPaint(this, R.color.heart_fill);

        datePaint = new PaintHolder(false) {
            @Override
            protected void configure(Paint paint) {
                Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-Regular.ttf");
                paint.setTypeface(roboto);
                paint.setColor(Workarounds.getColor(Love39WatchFace.this, R.color.date_fill));
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(getResources().getDimension(R.dimen.date_size));
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
            }
        };

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
    protected void onLayout(WatchShape shape, Rect screenBounds, WindowInsets screenInsets) {
        super.onLayout(shape, screenBounds, screenInsets);

        onWatchStyleChanged();
    }

    private void onWatchStyleChanged() {
        Rect screenBounds = new Rect(0, 0, getWidth(), getHeight());

        background.setBounds(screenBounds);

        Ticks ticks = timeStyle.getTicks();
        ticks.updateShape(getWatchShape());
        ticks.setBounds(screenBounds);

        Hands hands = timeStyle.getHands();
        hands.setBounds(screenBounds);

        heart = new Heart(this);
        int heartX = screenBounds.centerX();
        int heartY = screenBounds.height() * 45 / 64;
        float heartWidth = getResources().getDimension(R.dimen.analog_heart_width);
        float heartHeight = getResources().getDimension(R.dimen.analog_heart_height);
        heart.updateBounds(
                heartX - heartWidth / 2,
                heartY - heartHeight / 2,
                heartX + heartWidth / 2,
                heartY + heartHeight / 2);

        //TODO Insets, for the chin I guess.

        updateWatchMode(new WatchModeHelper(getCurrentWatchMode()));
    }

    @Override
    protected void onWatchModeChanged(WatchMode watchMode) {
        super.onWatchModeChanged(watchMode);
        updateWatchMode(new WatchModeHelper(watchMode));
    }

    @Override
    public void updateWatchMode(WatchModeHelper mode) {
        background.updateWatchMode(mode);
        datePaint.updateWatchMode(mode);
        timeStyle.getTicks().updateWatchMode(mode);
        timeStyle.getHands().updateWatchMode(mode);
    }

    @Override
    protected void onTimeChanged(WatchFaceTime oldTime, WatchFaceTime newTime) {
        super.onTimeChanged(oldTime, newTime);

        timeStyle.getTime().setTo(newTime);
        dateStyle.getTime().setTo(newTime);
    }

    @Override
    protected void onWatchFaceConfigChanged(SharedPreferences sharedPreferences, String key) {
        updateConfiguration(sharedPreferences);
    }

    private void updateConfiguration(SharedPreferences preferences) {
        if (preferences.getBoolean(ConfigKeys.DOZENAL_TIME_KEY, false)) {
            timeStyle = new DozenalTimeStyle(this);
        } else {
            timeStyle = new ClassicTimeStyle(this);
        }

        if (preferences.getBoolean(ConfigKeys.DOZENAL_CALENDAR_KEY, false)) {
            dateStyle = new DozenalDateStyle(this);
        } else {
            dateStyle = new ClassicDateStyle(this);
        }

        onWatchStyleChanged();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        background.draw(canvas);

        float centerX = canvas.getWidth() / 2;
        float centerY = canvas.getHeight() / 2;

        Time time = dateStyle.getTime();
        canvas.drawText(
                dateStyle.getDateFormat().formatDate(time),
                centerX, centerY / 2, datePaint.getPaint());

        Ticks ticks = timeStyle.getTicks();
        ticks.draw(canvas);

        WatchMode watchMode = getCurrentWatchMode();
        if (watchMode == WatchMode.INTERACTIVE) {
            canvas.drawPath(heart.getPath(), heartGlowPaint);
            heart.draw(canvas);
        }

        Hands hands = timeStyle.getHands();
        hands.updateTime(time);
        hands.draw(canvas);
    }
}
