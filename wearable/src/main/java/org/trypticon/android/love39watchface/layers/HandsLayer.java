/*
 * Copyright © 2016-2017 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 *
 * This file additionally may contain code which programmatically renders
 * representations of elements of Hatsune Miku's design. Such art-as-code is
 * subject to the terms and conditions of the Creative Commons -
 * Attribution-NonCommercial, 3.0 Unported (CC BY-NC) licence.
 * See the Copying.CC_BY_NC file for more details.
 */

package org.trypticon.android.love39watchface.layers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.ustwo.clockwise.WatchMode;

import org.trypticon.android.love39watchface.R;
import org.trypticon.android.love39watchface.framework.PaintHolder;
import org.trypticon.android.love39watchface.framework.PaintUtils;
import org.trypticon.android.love39watchface.framework.WatchModeHelper;
import org.trypticon.android.love39watchface.framework.Workarounds;
import org.trypticon.android.love39watchface.time.MultiTime;
import org.trypticon.android.love39watchface.time.Time;
import org.trypticon.android.love39watchface.time.TimeSystem;

/**
 * Drawable to draw the hands.
 */
class HandsLayer extends Layer {

    private final Context context;
    private final TimeSystem timeSystem;
    private final boolean zeroAtTop;
    private final boolean hasThirds;

    private Hand hourHand;
    private Hand minuteHand;
    private Hand secondHand;
    private Hand thirdHand;

    private final PaintHolder centrePaint;

    private final Path centrePath = new Path();
    private final Path handGlowPath;
    private final Paint handGlowPaint;

    // Default to interactive for rendering in other places, like the config screen.
    private WatchModeHelper watchMode = new WatchModeHelper(WatchMode.INTERACTIVE);

    HandsLayer(final Context context, TimeSystem timeSystem, boolean zeroAtTop, boolean hasThirds) {
        this.context = context;
        this.timeSystem = timeSystem;
        this.zeroAtTop = zeroAtTop;
        this.hasThirds = hasThirds;

        centrePaint = new PaintHolder(true) {
            @Override
            protected void configure(Paint paint) {
                paint.setColor(Workarounds.getColor(context, R.color.analog_centre_fill));
                paint.setAntiAlias(true);
                paint.setStrokeWidth(context.getResources().getDimension(R.dimen.hand_stroke_width));
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
            }
        };

        handGlowPaint = PaintUtils.createGlowPaint(context, R.color.analog_centre_fill, 400 * Proportions.HAND_GLOW_WIDTH);
        handGlowPath = new Path();

        createHands();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        createHands();
    }

    private void createHands() {
        Rect bounds = getBounds();
        int centerX = bounds.centerX();
        int centerY = bounds.centerY();
        float width = bounds.width();

        float hourLength   = centerX - width * Proportions.HOUR_HAND_FROM_EDGE;
        float minuteLength = centerX - width * Proportions.MINUTE_HAND_FROM_EDGE;
        float secondLength = centerX - width * Proportions.SECOND_HAND_FROM_EDGE;
        float thirdLength  = centerX - width * Proportions.THIRD_HAND_FROM_EDGE;

        float centreRadius = width * Proportions.CENTRE_RADIUS;
        centrePath.reset();
        centrePath.addCircle(centerX, centerY, centreRadius, Path.Direction.CCW);

        float largeHandWidth = bounds.width() * Proportions.LARGE_HAND_WIDTH;
        float smallHandWidth = bounds.width() * Proportions.SMALL_HAND_WIDTH;

        float handStartRadius = width * Proportions.HAND_START_RADIUS;

        float glowWidth = width * Proportions.HAND_GLOW_WIDTH;
        handGlowPaint.setStrokeWidth(glowWidth);

        hourHand = new Hand(
                context,
                largeHandWidth, handStartRadius, hourLength,
                R.color.analog_hands_fill,
                centerX, centerY, true, false);
        minuteHand = new Hand(
                context,
                largeHandWidth, handStartRadius, minuteLength,
                R.color.analog_hands_fill,
                centerX, centerY, true, true);
        secondHand = new Hand(
                context,
                smallHandWidth, handStartRadius, secondLength,
                hasThirds ? R.color.analog_second_hand_fill : R.color.analog_third_hand_fill,
                centerX, centerY, false, false);
        thirdHand = new Hand(
                context,
                smallHandWidth, handStartRadius, thirdLength,
                R.color.analog_third_hand_fill,
                centerX, centerY, false, false);
    }

    @Override
    public void updateTime(MultiTime multiTime) {
        Time time = multiTime.getTime(timeSystem);

        int angleShift = zeroAtTop ? 180 : 0;
        hourHand.updateAngle(time.getHourTurns() * 360 + angleShift);
        minuteHand.updateAngle(time.getMinuteTurns() * 360 + angleShift);

        if (watchMode.isInteractive()) {
            secondHand.updateAngle(time.getSecondTurns() * 360 + angleShift);
            if (time.hasThirds()) {
                thirdHand.updateAngle(time.getThirdTurns() * 360 + angleShift);
            }
        }
    }

    @Override
    public void updateWatchMode(WatchModeHelper mode) {
        watchMode = mode;
        centrePaint.updateWatchMode(mode);
        hourHand.updateWatchMode(mode);
        minuteHand.updateWatchMode(mode);
        secondHand.updateWatchMode(mode);
        thirdHand.updateWatchMode(mode);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (watchMode.isInteractive()) {
            handGlowPath.reset();
            handGlowPath.addPath(hourHand.getPath());
            handGlowPath.addPath(minuteHand.getPath());
            handGlowPath.addPath(centrePath);
            canvas.drawPath(handGlowPath, handGlowPaint);
        }

        hourHand.draw(canvas);
        minuteHand.draw(canvas);

        if (watchMode.isInteractive()) {
            secondHand.draw(canvas);
            if (hasThirds) {
                thirdHand.draw(canvas);
            }
        }

        canvas.drawPath(centrePath, centrePaint.getPaint());
    }

}
