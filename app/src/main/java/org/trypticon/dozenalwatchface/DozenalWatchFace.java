package org.trypticon.dozenalwatchface;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Encapsulates the drawing of the watch face itself.
 */
class DozenalWatchFace extends Drawable {
    private final Context context;

    private final Paint clearPaint;
    private final Paint realBackgroundPaint;
    private Paint backgroundPaint;

    private Hand hourHand;
    private Hand minuteHand;
    private Hand secondHand;
    private Hand thirdHand;

    private final Paint centrePaint;
    private final float centreRadius;
    private final Paint blurPaint;

    private final Ticks ticks;

    private final Paint datePaint;

    private boolean ambient;
    private boolean highQuality;
    private final DozenalTime dozenalTime = new DozenalTime();
    private final DozenalDateFormat dozenalDateFormat = new DozenalDateFormat();

    DozenalWatchFace(Context context) {
        this.context = context;

        clearPaint = new Paint();
        clearPaint.setColor(Color.BLACK);

        realBackgroundPaint = new Paint();
        realBackgroundPaint.setColor(Workarounds.getColor(context, R.color.analog_background));

        centrePaint = new Paint();
        centrePaint.setColor(Workarounds.getColor(context, R.color.analog_centre_fill));
        centrePaint.setAntiAlias(true);
        centrePaint.setStrokeWidth(context.getResources().getDimension(R.dimen.analog_centre_stroke));
        centrePaint.setStrokeCap(Paint.Cap.ROUND);
        centrePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        float shadowWidth = context.getResources().getDimension(R.dimen.analog_blur_width);
        blurPaint = new Paint(centrePaint);
        blurPaint.setColor((centrePaint.getColor() & 0xFFFFFF) | 0x40000000);
        blurPaint.setStrokeWidth(shadowWidth);
        blurPaint.setMaskFilter(new BlurMaskFilter(shadowWidth, BlurMaskFilter.Blur.NORMAL));

        centreRadius = context.getResources().getDimension(R.dimen.analog_centre_radius);

        ticks = new Ticks(context);

        Typeface roboto = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        datePaint = new Paint();
        datePaint.setTypeface(roboto);
        datePaint.setColor(Workarounds.getColor(context, R.color.date_fill));
        datePaint.setTextAlign(Paint.Align.CENTER);
        datePaint.setTextSize(context.getResources().getDimension(R.dimen.date_size));
    }

    @Override
    public void draw(Canvas canvas) {
        dozenalTime.setToNow();

        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

        Rect bounds = getBounds();
        float centerX = bounds.centerX();
        float centerY = bounds.centerY();

        canvas.drawText(
                dozenalDateFormat.formatDate(dozenalTime),
                centerX, centerY * 1 / 2, datePaint);

        ticks.draw(canvas);

        hourHand.updateAngle(dozenalTime.getHourTurns() * 360);
        minuteHand.updateAngle(dozenalTime.getMinuteTurns() * 360);

        if (!ambient) {
            Path path = new Path();
            path.addPath(hourHand.getPath());
            path.addPath(minuteHand.getPath());
            path.addCircle(centerX, centerY, centreRadius, Path.Direction.CCW);
            canvas.drawPath(path, blurPaint);
        }

        hourHand.draw(canvas);
        minuteHand.draw(canvas);

        if (!ambient) {
            secondHand.updateAngle(dozenalTime.getSecondTurns() * 360);
            secondHand.draw(canvas);
            thirdHand.updateAngle(dozenalTime.getThirdTurns() * 360);
            thirdHand.draw(canvas);
        }

        canvas.drawCircle(centerX, centerY, centreRadius, centrePaint);
    }

    void updateShape(boolean round) {
        ticks.updateShape(round);
    }

    void updateLocale(Locale locale) {
        dozenalDateFormat.updateLocale(locale);
    }

    void updateTimeZone(TimeZone timeZone) {
        dozenalTime.updateTimeZone(timeZone);
        dozenalTime.setToNow();
    }

    void updateAmbient(boolean lowBitAmbient, boolean ambient) {
        boolean highQuality = !(ambient && lowBitAmbient);
        if (this.ambient != ambient || this.highQuality != highQuality) {
            this.ambient = ambient;
            this.highQuality = highQuality;
            updateRenderingProperties();
        }
    }

    private void updateRenderingProperties() {
        backgroundPaint = highQuality ? realBackgroundPaint : clearPaint;

        hourHand.updateHighQuality(highQuality);
        minuteHand.updateHighQuality(highQuality);
        secondHand.updateHighQuality(highQuality);
        thirdHand.updateHighQuality(highQuality);
        centrePaint.setAntiAlias(highQuality);
        ticks.updateHighQuality(highQuality);
        datePaint.setAntiAlias(highQuality);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        ticks.setBounds(bounds);

        float centerX = bounds.centerX();
        float centerY = bounds.centerY();

        float thirdLength = centerX - 30;
        float secondLength = centerX - 35;
        float minuteLength = centerX - 40;
        float hourLength = centerX - 60;

        hourHand = new Hand(
                context,
                R.dimen.analog_hand_width,
                R.color.analog_hands_fill,
                R.dimen.analog_hand_stroke_width,
                centerX, centerY, hourLength, true, false);
        minuteHand = new Hand(
                context,
                R.dimen.analog_hand_width,
                R.color.analog_hands_fill,
                R.dimen.analog_hand_stroke_width,
                centerX, centerY, minuteLength, true, true);
        secondHand = new Hand(
                context,
                R.dimen.analog_second_hand_width,
                R.color.analog_second_hand_fill,
                R.dimen.analog_second_hand_stroke_width,
                centerX, centerY, secondLength, false, false);
        thirdHand = new Hand(
                context,
                R.dimen.analog_third_hand_width,
                R.color.analog_third_hand_fill,
                R.dimen.analog_third_hand_stroke_width,
                centerX, centerY, thirdLength, false, false);

        updateRenderingProperties();
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
