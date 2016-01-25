package org.trypticon.android.love39watchface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;

import org.trypticon.android.love39watchface.framework.PaintHolder;
import org.trypticon.android.love39watchface.framework.WatchModeHelper;
import org.trypticon.android.love39watchface.framework.Workarounds;

/**
 * Encapsulation of information about a single hand.
 */
class Hand {
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
         @DimenRes int widthId, @ColorRes int fillColorId,
         @DimenRes final int strokeWidthId,
         float centerX, float centerY, float handLength, boolean pointy, boolean clip) {

        this.centerX = centerX;
        this.centerY = centerY;

        float halfHandWidth = context.getResources().getDimension(widthId) / 2;

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
                paint.setStrokeWidth(context.getResources().getDimension(strokeWidthId));
                paint.setStrokeCap(Paint.Cap.BUTT);
                paint.setStyle(Paint.Style.STROKE);
                paint.setAntiAlias(true);
            }
        };

        clipPaint = new PaintHolder(false) {
            @Override
            protected void configure(Paint paint) {
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(2.0f);
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

        float centreRadius = context.getResources().getDimension(R.dimen.analog_centre_hand_start_radius);
        float centreDegrees = (float) Math.toDegrees(Math.asin(halfHandWidth / centreRadius));
        float startOffset = (float) Math.sqrt(centreRadius * centreRadius - halfHandWidth * halfHandWidth);
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
                -centreRadius, -centreRadius, centreRadius, centreRadius,
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

    void updateWatchMode(WatchModeHelper mode) {
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
