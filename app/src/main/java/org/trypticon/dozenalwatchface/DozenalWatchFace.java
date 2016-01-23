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

    private Paint datePaint;

    private WatchStyle watchStyle;

    @Override
    public void onCreate() {
        super.onCreate();


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

        watchStyle = new DozenalWatchStyle(this);
//        watchStyle = new GregorianWatchStyle(this);

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

        Ticks ticks = watchStyle.getTicks();
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
                watchStyle.getTime().hasThirds() ? R.color.analog_second_hand_fill
                        : R.color.analog_third_hand_fill,
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
        watchStyle.getTicks().updateHighQuality(highQuality);
        datePaint.setAntiAlias(highQuality);
    }

    @Override
    protected void onTimeChanged(WatchFaceTime oldTime, WatchFaceTime newTime) {
        super.onTimeChanged(oldTime, newTime);

        watchStyle.getTime().setTo(newTime);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

        float centerX = canvas.getWidth() / 2;
        float centerY = canvas.getHeight() / 2;

        Time time = watchStyle.getTime();
        canvas.drawText(
                watchStyle.getDateFormat().formatDate(time),
                centerX, centerY * 1 / 2, datePaint);

        Ticks ticks = watchStyle.getTicks();
        ticks.draw(canvas);

        int angleShift = ticks.isZeroAtTop() ? 180 : 0;
        hourHand.updateAngle(time.getHourTurns() * 360 + angleShift);
        minuteHand.updateAngle(time.getMinuteTurns() * 360 + angleShift);

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
            secondHand.updateAngle(time.getSecondTurns() * 360 + angleShift);
            secondHand.draw(canvas);
            if (time.hasThirds()) {
                thirdHand.updateAngle(time.getThirdTurns() * 360 + angleShift);
                thirdHand.draw(canvas);
            }
        }

        canvas.drawCircle(centerX, centerY, centreRadius, centrePaint);
    }
}
