package org.trypticon.dozenalwatchface;

import android.content.SharedPreferences;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.annotation.ColorRes;
import android.view.WindowInsets;

import com.ustwo.clockwise.WatchFaceTime;
import com.ustwo.clockwise.WatchMode;
import com.ustwo.clockwise.WatchShape;

import org.trypticon.dozenalwatchface.config.ConfigKeys;
import org.trypticon.dozenalwatchface.framework.ConfigurableWatchFace;
import org.trypticon.dozenalwatchface.time.Time;

/**
 * The main watch face.
 */
public class DozenalWatchFace extends ConfigurableWatchFace {
    // I originally determined 20 experimentally.
    // The ustwo examples use 33, so that might be better.
    // The correct value probably depends on screen resolution!
    private static final long INTERACTIVE_UPDATE_RATE_MS = 33;

    private PaintHolder backgroundPaint;

    private Hand hourHand;
    private Hand minuteHand;
    private Hand secondHand;
    private Hand thirdHand;

    private PaintHolder centrePaint;
    private float centreRadius;

    private Heart heart;
    private Paint heartGlowPaint;

    private Path handGlowPath;
    private Paint handGlowPaint;

    private PaintHolder datePaint;

    private TimeStyle timeStyle;
    private DateStyle dateStyle;

    @Override
    public void onCreate() {
        super.onCreate();

        backgroundPaint = new PaintHolder(true) {
            @Override
            protected void configure(Paint paint) {
                paint.setColor(Workarounds.getColor(DozenalWatchFace.this, R.color.analog_background));
            }
        };

        centrePaint = new PaintHolder(true) {
            @Override
            protected void configure(Paint paint) {
                paint.setColor(Workarounds.getColor(DozenalWatchFace.this, R.color.analog_centre_fill));
                paint.setAntiAlias(true);
                paint.setStrokeWidth(getResources().getDimension(R.dimen.analog_centre_stroke));
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
            }
        };

        handGlowPaint = createGlowPaint(R.color.analog_centre_fill);
        handGlowPath = new Path();

        heartGlowPaint = createGlowPaint(R.color.heart_fill);
        centreRadius = getResources().getDimension(R.dimen.analog_centre_radius);

        datePaint = new PaintHolder(false) {
            @Override
            protected void configure(Paint paint) {
                Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-Regular.ttf");
                paint.setTypeface(roboto);
                paint.setColor(Workarounds.getColor(DozenalWatchFace.this, R.color.date_fill));
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(getResources().getDimension(R.dimen.date_size));
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
            }
        };

        updateConfiguration(PreferenceManager.getDefaultSharedPreferences(this));
    }

    private Paint createGlowPaint(@ColorRes int baseColorKey) {
        float glowWidth = getResources().getDimension(R.dimen.analog_blur_width);
        Paint paint = new Paint();
        paint.setColor((Workarounds.getColor(this, baseColorKey) & 0xFFFFFF) | 0x40000000);
        paint.setMaskFilter(new BlurMaskFilter(glowWidth, BlurMaskFilter.Blur.NORMAL));
        paint.setStrokeWidth(glowWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        return paint;
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

        Ticks ticks = timeStyle.getTicks();
        ticks.updateShape(getWatchShape() == WatchShape.CIRCLE);
        ticks.setBounds(screenBounds);
        ticks.updateWatchMode(new WatchModeHelper(getCurrentWatchMode()));

        int centerX = screenBounds.centerX();
        int centerY = screenBounds.centerY();

        float thirdLength = centerX - 30;
        float secondLength = centerX - 35;
        float minuteLength = centerX - 40;
        float hourLength = centerX - 60;

        hourHand = new Hand(
                this,
                R.dimen.analog_hand_width,
                R.color.analog_hands_fill,
                R.dimen.analog_hand_stroke_width,
                centerX, centerY, hourLength, true, false);
        minuteHand = new Hand(
                this,
                R.dimen.analog_hand_width,
                R.color.analog_hands_fill,
                R.dimen.analog_hand_stroke_width,
                centerX, centerY, minuteLength, true, true);
        secondHand = new Hand(
                this,
                R.dimen.analog_second_hand_width,
                timeStyle.getTime().hasThirds() ? R.color.analog_second_hand_fill
                        : R.color.analog_third_hand_fill,
                R.dimen.analog_second_hand_stroke_width,
                centerX, centerY, secondLength, false, false);
        thirdHand = new Hand(
                this,
                R.dimen.analog_third_hand_width,
                R.color.analog_third_hand_fill,
                R.dimen.analog_third_hand_stroke_width,
                centerX, centerY, thirdLength, false, false);

        heart = new Heart(this);
        int heartY = screenBounds.height() * 45 / 64;
        float heartWidth = getResources().getDimension(R.dimen.analog_heart_width);
        float heartHeight = getResources().getDimension(R.dimen.analog_heart_height);
        heart.updateBounds(
                centerX - heartWidth / 2,
                heartY - heartHeight / 2,
                centerX + heartWidth / 2,
                heartY + heartHeight / 2);

        //TODO Insets, for the chin I guess.

        updateRenderingProperties();
    }

    @Override
    protected void onWatchModeChanged(WatchMode watchMode) {
        super.onWatchModeChanged(watchMode);

        updateRenderingProperties();
    }

    private void updateRenderingProperties() {
        WatchModeHelper watchModeHelper = new WatchModeHelper(getCurrentWatchMode());

        backgroundPaint.updateWatchMode(watchModeHelper);
        centrePaint.updateWatchMode(watchModeHelper);
        datePaint.updateWatchMode(watchModeHelper);
        hourHand.updateWatchMode(watchModeHelper);
        minuteHand.updateWatchMode(watchModeHelper);
        secondHand.updateWatchMode(watchModeHelper);
        thirdHand.updateWatchMode(watchModeHelper);
        timeStyle.getTicks().updateWatchMode(watchModeHelper);
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
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint.getPaint());

        float centerX = canvas.getWidth() / 2;
        float centerY = canvas.getHeight() / 2;

        Time time = dateStyle.getTime();
        canvas.drawText(
                dateStyle.getDateFormat().formatDate(time),
                centerX, centerY * 9 / 16, datePaint.getPaint());

        Ticks ticks = timeStyle.getTicks();
        ticks.draw(canvas);

        int angleShift = ticks.isZeroAtTop() ? 180 : 0;
        hourHand.updateAngle(time.getHourTurns() * 360 + angleShift);
        minuteHand.updateAngle(time.getMinuteTurns() * 360 + angleShift);

        WatchMode watchMode = getCurrentWatchMode();
        if (watchMode == WatchMode.INTERACTIVE) {
            canvas.drawPath(heart.getPath(), heartGlowPaint);
            heart.draw(canvas);

            handGlowPath.reset();
            handGlowPath.addPath(hourHand.getPath());
            handGlowPath.addPath(minuteHand.getPath());
            handGlowPath.addCircle(centerX, centerY, centreRadius, Path.Direction.CCW);
            canvas.drawPath(handGlowPath, handGlowPaint);
        }

        hourHand.draw(canvas);
        minuteHand.draw(canvas);

        if (watchMode == WatchMode.INTERACTIVE) {
            secondHand.updateAngle(time.getSecondTurns() * 360 + angleShift);
            secondHand.draw(canvas);
            if (time.hasThirds()) {
                thirdHand.updateAngle(time.getThirdTurns() * 360 + angleShift);
                thirdHand.draw(canvas);
            }
        }

        canvas.drawCircle(centerX, centerY, centreRadius, centrePaint.getPaint());
    }
}
