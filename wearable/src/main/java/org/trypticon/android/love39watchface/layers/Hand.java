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
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorRes;

import org.trypticon.android.love39watchface.R;
import org.trypticon.android.love39watchface.framework.PaintHolder;
import org.trypticon.android.love39watchface.framework.WatchModeAware;
import org.trypticon.android.love39watchface.framework.WatchModeHelper;
import org.trypticon.android.love39watchface.framework.Workarounds;

/**
 * Encapsulation of information about a single hand.
 */
class Hand implements WatchModeAware {
    private final float centerX;
    private final float centerY;

    private final Path untransformedPath;
    private final Path path = new Path();
    private final Path untransformedClipPath;
    private final Path clipPath;

    private final PaintHolder strokePaint;
    private final PaintHolder fillPaint;
    private final PaintHolder clipPaint;

    private final Matrix matrix = new Matrix();

    Hand(final Context context,
         float handWidth, float handStartRadius, float handLength,
         @ColorRes int fillColorId,
         float centerX, float centerY, boolean pointy, boolean clip) {

        this.centerX = centerX;
        this.centerY = centerY;

        final float handStrokeWidth = context.getResources().getDimension(R.dimen.hand_stroke_width);
        final float clipStrokeWidth = context.getResources().getDimension(R.dimen.clip_stroke_width);
        float halfHandWidth = handWidth / 2;

        final int fillColor = Workarounds.getColor(context, fillColorId);

        fillPaint = new PaintHolder(true) {
            @Override
            protected void configure(Paint paint) {
                paint.setColor(fillColor);
                paint.setStyle(Paint.Style.FILL);
                paint.setAntiAlias(false);
            }
        };

        strokePaint = new PaintHolder(false) {
            @Override
            protected void configure(Paint paint) {
                paint.setColor(fillColor);
                paint.setStrokeWidth(handStrokeWidth);
                paint.setStrokeCap(Paint.Cap.BUTT);
                paint.setStyle(Paint.Style.STROKE);
                paint.setAntiAlias(true);
            }
        };

        clipPaint = new PaintHolder(false) {
            @Override
            protected void configure(Paint paint) {
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(clipStrokeWidth);
                paint.setStrokeCap(Paint.Cap.SQUARE);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setAntiAlias(true);
            }

            @Override
            protected void configureForPaintingOnBlack(Paint paint) {
                // Outline the same as the tie, otherwise it's black on black.
                float[] hsv = new float[3];
                Color.colorToHSV(fillColor, hsv);
                hsv[1] = 0;
                paint.setColor(Color.HSVToColor(Color.alpha(fillColor), hsv));
            }
        };

        untransformedPath = new Path();

        float centreDegrees = (float) Math.toDegrees(Math.asin(halfHandWidth / handStartRadius));
        float startOffset = (float) Math.sqrt(handStartRadius * handStartRadius - halfHandWidth * halfHandWidth);
        float centreStartDegrees = 90.0f - centreDegrees;
        float centreSweepDegrees = 2.0f * centreDegrees;

        untransformedPath.moveTo(-halfHandWidth, startOffset);
        if (pointy) {
            untransformedPath.lineTo(-halfHandWidth, handLength - halfHandWidth);
            untransformedPath.lineTo(0, handLength);
            untransformedPath.lineTo(halfHandWidth, handLength - halfHandWidth);
        } else {
            untransformedPath.lineTo(-halfHandWidth, handLength);
            untransformedPath.lineTo(halfHandWidth, handLength);
        }
        untransformedPath.lineTo(halfHandWidth, startOffset);
        untransformedPath.arcTo(
                -handStartRadius, -handStartRadius, handStartRadius, handStartRadius,
                centreStartDegrees, centreSweepDegrees, false);
        untransformedPath.close();

        if (clip) {
            untransformedClipPath = new Path();
            clipPath = new Path();

            untransformedClipPath.moveTo(-halfHandWidth, handLength - halfHandWidth * 4);
            untransformedClipPath.lineTo(halfHandWidth / 2, handLength - halfHandWidth * 4);
            untransformedClipPath.lineTo(halfHandWidth / 2, handLength - halfHandWidth * 4 + halfHandWidth / 2);
            untransformedClipPath.lineTo(-halfHandWidth, handLength - halfHandWidth * 4 + halfHandWidth / 2);
            untransformedClipPath.close();

            untransformedClipPath.moveTo(-halfHandWidth, handLength - halfHandWidth * 3);
            untransformedClipPath.lineTo(halfHandWidth / 2, handLength - halfHandWidth * 3);
            untransformedClipPath.lineTo(halfHandWidth / 2, handLength - halfHandWidth * 3 + halfHandWidth / 2);
            untransformedClipPath.lineTo(-halfHandWidth, handLength - halfHandWidth * 3 + halfHandWidth / 2);
            untransformedClipPath.close();
        } else {
            untransformedClipPath = null;
            clipPath = null;
        }
    }

    @Override
    public void updateWatchMode(WatchModeHelper mode) {
        fillPaint.updateWatchMode(mode);
        strokePaint.updateWatchMode(mode);
        clipPaint.updateWatchMode(mode);
    }

    void updateAngle(float angleDegrees) {
        matrix.reset();
        matrix.setRotate(angleDegrees, 0, 0);
        matrix.postTranslate(centerX, centerY);

        untransformedPath.transform(matrix, path);
        if (untransformedClipPath != null) {
            untransformedClipPath.transform(matrix, clipPath);
        }
    }

    Path getPath() {
        return path;
    }

    void draw(Canvas canvas) {
        // Separately paints fill and stroke because it renders incorrectly otherwise due to some
        // kind of bug I can't figure out.
        canvas.drawPath(path, fillPaint.getPaint());
        canvas.drawPath(path, strokePaint.getPaint());

        if (clipPath != null) {
            canvas.drawPath(clipPath, clipPaint.getPaint());
        }
    }
}
