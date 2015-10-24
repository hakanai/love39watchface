package org.trypticon.dozenalwatchface;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import java.util.TimeZone;

/**
 * Encapsulates the drawing of the watch face itself.
 */
class DozenalWatchFace extends Drawable {
    private final Resources resources;

    private Paint clearPaint;
    private Paint realBackgroundPaint;
    private Paint backgroundPaint;

    private float lastWidth;
    private float lastHeight;
    private float centerX;
    private float centerY;
    private Hand hourHand;
    private Hand minuteHand;
    private Hand secondHand;
    private Hand thirdHand;

    private Paint centreFill;
    private float centreRadius;

    private Drawable[] digits;
    private Paint primaryTickStroke;
    private Paint secondaryTickStroke;

    private boolean ambient;
    private boolean highQuality;
    private DozenalTime dozenalTime;

    DozenalWatchFace(Resources resources, Resources.Theme theme) {
        this.resources = resources;

        clearPaint = new Paint();
        clearPaint.setColor(Color.BLACK);

        realBackgroundPaint = new Paint();
        realBackgroundPaint.setColor(Workarounds.getColor(resources, R.color.analog_background));

        centreFill = new Paint();
        centreFill.setColor(Workarounds.getColor(resources, R.color.analog_centre_fill));
        centreFill.setAntiAlias(true);
        centreFill.setStrokeWidth(resources.getDimension(R.dimen.analog_centre_stroke));
        centreFill.setStrokeCap(Paint.Cap.ROUND);
        centreFill.setStyle(Paint.Style.FILL_AND_STROKE);
        centreFill.setShadowLayer(
                resources.getDimension(R.dimen.analog_hand_shadow_width),
                0, 0, Workarounds.getColor(resources, R.color.analog_centre_fill));

        centreRadius = resources.getDimension(R.dimen.analog_centre_radius);

        digits = new Drawable[] {
                resources.getDrawable(R.drawable.digit0, theme),
                resources.getDrawable(R.drawable.digit1, theme),
                resources.getDrawable(R.drawable.digit2, theme),
                resources.getDrawable(R.drawable.digit3, theme),
                resources.getDrawable(R.drawable.digit4, theme),
                resources.getDrawable(R.drawable.digit5, theme),
                resources.getDrawable(R.drawable.digit6, theme),
                resources.getDrawable(R.drawable.digit7, theme),
                resources.getDrawable(R.drawable.digit8, theme),
                resources.getDrawable(R.drawable.digit9, theme),
                resources.getDrawable(R.drawable.digitdec, theme),
                resources.getDrawable(R.drawable.digitel, theme),
        };

        primaryTickStroke = new Paint();
        primaryTickStroke.setColor(Workarounds.getColor(resources, R.color.analog_primary_tick_stroke));
        primaryTickStroke.setStrokeWidth(resources.getDimension(R.dimen.analog_primary_tick_stroke));
        primaryTickStroke.setAntiAlias(true);
        primaryTickStroke.setStrokeCap(Paint.Cap.SQUARE);
        primaryTickStroke.setStyle(Paint.Style.FILL_AND_STROKE);

        secondaryTickStroke = new Paint();
        secondaryTickStroke.setColor(Workarounds.getColor(resources, R.color.analog_secondary_tick_stroke));
        secondaryTickStroke.setStrokeWidth(resources.getDimension(R.dimen.analog_secondary_tick_stroke));
        secondaryTickStroke.setAntiAlias(true);
        secondaryTickStroke.setStrokeCap(Paint.Cap.SQUARE);
        secondaryTickStroke.setStyle(Paint.Style.FILL_AND_STROKE);

        dozenalTime = new DozenalTime();
    }

    @Override
    public void draw(Canvas canvas) {
        dozenalTime.setToNow();

        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

        if (!ambient) {
            thirdHand.draw(canvas, dozenalTime.getThirdTurns() * 360);
            secondHand.draw(canvas, dozenalTime.getSecondTurns() * 360);
        }

        minuteHand.draw(canvas, dozenalTime.getMinuteTurns() * 360);
        hourHand.draw(canvas, dozenalTime.getHourTurns() * 360);

        canvas.drawCircle(centerX, centerY, centreRadius, centreFill);

        // primary ticks
        for (int digit = 0, degrees = 0; digit < 12; digit++, degrees += 360/12) {

            Drawable digitDrawable = digits[digit];
            float digitDistance = centerX - digitDrawable.getIntrinsicHeight();
            float digitX = (centerX - (float) (digitDistance * Math.sin(Math.toRadians(degrees))));
            float digitY = (centerX + (float) (digitDistance * Math.cos(Math.toRadians(degrees))));

            digitDrawable.setBounds(
                    (int) (digitX - digitDrawable.getIntrinsicWidth() / 2.0f),
                    (int) (digitY - digitDrawable.getIntrinsicHeight() / 2.0f),
                    (int) (digitX + digitDrawable.getIntrinsicWidth() / 2.0f),
                    (int) (digitY + digitDrawable.getIntrinsicHeight() / 2.0f));
            digitDrawable.draw(canvas);

            canvas.save();
            canvas.translate(centerX, centerY);
            canvas.rotate(degrees);

            canvas.drawLine(0, centerX + 2, 0, centerX * 2, primaryTickStroke);

            // secondary ticks
            for (int i = 1; i < 6; i++) {
                canvas.rotate(360/12/6);
                canvas.drawLine(0, centerX - 8, 0, centerX * 2, secondaryTickStroke);
            }

            canvas.restore();
        }
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
        centreFill.setAntiAlias(highQuality);
        primaryTickStroke.setAntiAlias(highQuality);
        secondaryTickStroke.setAntiAlias(highQuality);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        int width = bounds.width();
        int height = bounds.height();
        if (lastWidth != width || lastHeight != height) {

            centerX = width / 2f;
            centerY = height / 2f;
            float thirdLength = centerX - 30;
            float secondLength = centerX - 45;
            float minuteLength = centerX - 60;
            float hourLength = centerX - 80;

            hourHand = new Hand(
                    resources,
                    R.dimen.analog_hand_width,
                    R.color.analog_hands_fill,
                    R.dimen.analog_hand_stroke_width,
                    R.dimen.analog_hand_shadow_width,
                    centerX, centerY, hourLength);
            minuteHand = new Hand(
                    resources,
                    R.dimen.analog_hand_width,
                    R.color.analog_hands_fill,
                    R.dimen.analog_hand_stroke_width,
                    R.dimen.analog_hand_shadow_width,
                    centerX, centerY, minuteLength);
            secondHand = new Hand(
                    resources,
                    R.dimen.analog_second_hand_width,
                    R.color.analog_second_hand_fill,
                    R.dimen.analog_second_hand_stroke_width,
                    R.dimen.analog_second_hand_shadow_width,
                    centerX, centerY, secondLength);
            thirdHand = new Hand(
                    resources,
                    R.dimen.analog_third_hand_width,
                    R.color.analog_third_hand_fill,
                    R.dimen.analog_third_hand_stroke_width,
                    R.dimen.analog_third_hand_shadow_width,
                    centerX, centerY, thirdLength);

            updateRenderingProperties();

            lastWidth = width;
            lastHeight = height;
        }
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
