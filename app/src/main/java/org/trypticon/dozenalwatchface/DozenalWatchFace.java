package org.trypticon.dozenalwatchface;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.WindowInsets;

import com.ustwo.clockwise.WatchFace;
import com.ustwo.clockwise.WatchFaceTime;
import com.ustwo.clockwise.WatchMode;
import com.ustwo.clockwise.WatchShape;

import org.joda.time.DateTimeZone;

import java.util.Locale;

/**
 * The main watch face.
 */
public class DozenalWatchFace extends WatchFace {
    // I originally determined 20 experimentally.
    // The ustwo examples use 33, so that might be better.
    // The correct value probably depends on screen resolution!
    private static final long INTERACTIVE_UPDATE_RATE_MS = 33;

    private Paint clearPaint;
    private Paint realBackgroundPaint;
    private Paint backgroundPaint;

    private Hand hourHand;
    private Hand minuteHand;
    private Hand secondHand;
    private Hand thirdHand;

    private Paint centrePaint;
    private float centreRadius;

    private Path blurPath;
    private Paint blurPaint;

    private Ticks ticks;

    private Paint datePaint;

    private DozenalTime dozenalTime;
    private DozenalDateFormat dozenalDateFormat;

    @Override
    public void onCreate() {
        super.onCreate();

        dozenalTime = new DozenalTime();
        dozenalDateFormat = new DozenalDateFormat(Locale.getDefault());

        clearPaint = new Paint();
        clearPaint.setColor(Color.BLACK);

        realBackgroundPaint = new Paint();
        realBackgroundPaint.setColor(Workarounds.getColor(this, R.color.analog_background));

        centrePaint = new Paint();
        centrePaint.setColor(Workarounds.getColor(this, R.color.analog_centre_fill));
        centrePaint.setAntiAlias(true);
        centrePaint.setStrokeWidth(getResources().getDimension(R.dimen.analog_centre_stroke));
        centrePaint.setStrokeCap(Paint.Cap.ROUND);
        centrePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        float shadowWidth = getResources().getDimension(R.dimen.analog_blur_width);
        blurPaint = new Paint(centrePaint);
        blurPaint.setColor((centrePaint.getColor() & 0xFFFFFF) | 0x40000000);
        blurPaint.setStrokeWidth(shadowWidth);
        blurPaint.setMaskFilter(new BlurMaskFilter(shadowWidth, BlurMaskFilter.Blur.NORMAL));
        blurPath = new Path();

        centreRadius = getResources().getDimension(R.dimen.analog_centre_radius);

        ticks = new Ticks(this);

        Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        datePaint = new Paint();
        datePaint.setTypeface(roboto);
        datePaint.setColor(Workarounds.getColor(this, R.color.date_fill));
        datePaint.setTextAlign(Paint.Align.CENTER);
        datePaint.setTextSize(getResources().getDimension(R.dimen.date_size));
    }

    @Override
    protected long getInteractiveModeUpdateRate() {
        return INTERACTIVE_UPDATE_RATE_MS;
    }

    @Override
    protected void onLayout(WatchShape shape, Rect screenBounds, WindowInsets screenInsets) {
        super.onLayout(shape, screenBounds, screenInsets);

        ticks.updateShape(shape == WatchShape.CIRCLE);

        ticks.setBounds(screenBounds);

        float centerX = screenBounds.centerX();
        float centerY = screenBounds.centerY();

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
                R.color.analog_second_hand_fill,
                R.dimen.analog_second_hand_stroke_width,
                centerX, centerY, secondLength, false, false);
        thirdHand = new Hand(
                this,
                R.dimen.analog_third_hand_width,
                R.color.analog_third_hand_fill,
                R.dimen.analog_third_hand_stroke_width,
                centerX, centerY, thirdLength, false, false);

        //TODO Insets, for the chin I guess.

        updateRenderingProperties();
    }

    @Override
    protected void onWatchModeChanged(WatchMode watchMode) {
        super.onWatchModeChanged(watchMode);

        updateRenderingProperties();
    }

    private void updateRenderingProperties() {
        WatchMode watchMode = getCurrentWatchMode();

        backgroundPaint = watchMode == WatchMode.INTERACTIVE ? realBackgroundPaint : clearPaint;

        boolean highQuality = watchMode != WatchMode.LOW_BIT &&
                watchMode != WatchMode.LOW_BIT_BURN_IN;
        hourHand.updateHighQuality(highQuality);
        minuteHand.updateHighQuality(highQuality);
        secondHand.updateHighQuality(highQuality);
        thirdHand.updateHighQuality(highQuality);
        centrePaint.setAntiAlias(highQuality);
        ticks.updateHighQuality(highQuality);
        datePaint.setAntiAlias(highQuality);
    }

    @Override
    protected void onTimeChanged(WatchFaceTime oldTime, WatchFaceTime newTime) {
        super.onTimeChanged(oldTime, newTime);

        if (newTime.hasTimeZoneChanged(oldTime)) {
            dozenalTime.updateTimeZone(DateTimeZone.forID(newTime.timezone));
        }

        dozenalTime.setTo(newTime);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

        float centerX = canvas.getWidth() / 2;
        float centerY = canvas.getHeight() / 2;

        canvas.drawText(
                dozenalDateFormat.formatDate(dozenalTime),
                centerX, centerY * 1 / 2, datePaint);

        ticks.draw(canvas);

        hourHand.updateAngle(dozenalTime.getHourTurns() * 360);
        minuteHand.updateAngle(dozenalTime.getMinuteTurns() * 360);

        WatchMode watchMode = getCurrentWatchMode();
        if (watchMode == WatchMode.INTERACTIVE) {
            blurPath.reset();
            blurPath.addPath(hourHand.getPath());
            blurPath.addPath(minuteHand.getPath());
            blurPath.addCircle(centerX, centerY, centreRadius, Path.Direction.CCW);
            canvas.drawPath(blurPath, blurPaint);
        }

        hourHand.draw(canvas);
        minuteHand.draw(canvas);

        if (watchMode == WatchMode.INTERACTIVE) {
            secondHand.updateAngle(dozenalTime.getSecondTurns() * 360);
            secondHand.draw(canvas);
            thirdHand.updateAngle(dozenalTime.getThirdTurns() * 360);
            thirdHand.draw(canvas);
        }

        canvas.drawCircle(centerX, centerY, centreRadius, centrePaint);
    }
}
