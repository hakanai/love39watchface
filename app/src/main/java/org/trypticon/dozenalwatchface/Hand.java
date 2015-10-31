package org.trypticon.dozenalwatchface;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Encapsulation of information about a single hand.
 */
class Hand {
    private static final float startOffset = 12;

    private final float centerX;
    private final float centerY;
    private final float halfHandWidth;
    private final float handLength;

    private final Path path;
    private final Path clipPath;

    private final Paint fillPaint;
    private final Paint shadowPaint;
    private final Paint clipPaint;

    private boolean highQuality = true;

    Hand(Context context,
         int widthId, int fillColorId, int strokeWidthId, int shadowWidthId,
         float centerX, float centerY, float handLength, boolean pointy, boolean clip) {

        this.centerX = centerX;
        this.centerY = centerY;
        this.handLength = handLength;

        halfHandWidth = context.getResources().getDimension(widthId) / 2;

        int fillColor = Workarounds.getColor(context, fillColorId);

        fillPaint = new Paint();
        fillPaint.setColor(fillColor);
        fillPaint.setStrokeWidth(context.getResources().getDimension(strokeWidthId));
        fillPaint.setStrokeCap(Paint.Cap.BUTT);
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        fillPaint.setAntiAlias(true);

        // I found Paint#setShadowLayer to be a bit crap. :(
        float shadowWidth = context.getResources().getDimension(shadowWidthId);
        shadowPaint = new Paint(fillPaint);
        shadowPaint.setColor((fillColor & 0xFFFFFF) | 0x40000000);
        shadowPaint.setStrokeWidth(shadowWidth);
        shadowPaint.setMaskFilter(new BlurMaskFilter(shadowWidth, BlurMaskFilter.Blur.NORMAL));

        clipPaint = new Paint();
        clipPaint.setColor(Color.BLACK);
        clipPaint.setStrokeWidth(2.0f);
        clipPaint.setStrokeCap(Paint.Cap.SQUARE);
        clipPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        clipPaint.setAntiAlias(true);

        path = new Path();

        float centreRadius =
                (float) Math.sqrt(halfHandWidth * halfHandWidth + startOffset * startOffset);
        float centreDegrees = (float) Math.toDegrees(Math.atan(halfHandWidth / startOffset));
        float centreStartDegrees = 90.0f - centreDegrees;
        float centreSweepDegrees = 2.0f * centreDegrees;

        if (pointy) {
            path.moveTo(-halfHandWidth, startOffset);
            path.lineTo(-halfHandWidth, handLength - halfHandWidth);
            path.lineTo(0, handLength);
            path.lineTo(halfHandWidth, handLength - halfHandWidth);
            path.lineTo(halfHandWidth, startOffset);
            path.arcTo(
                    -centreRadius, -centreRadius, centreRadius, centreRadius,
                    centreStartDegrees, centreSweepDegrees, false);
            path.close();
        } else {
            path.moveTo(-halfHandWidth, startOffset);
            path.lineTo(-halfHandWidth, handLength);
            path.lineTo(halfHandWidth, handLength);
            path.lineTo(halfHandWidth, startOffset);
            path.arcTo(
                    -centreRadius, -centreRadius, centreRadius, centreRadius,
                    centreStartDegrees, centreSweepDegrees, false);
            path.close();
        }

        if (clip) {
            clipPath = new Path();

            clipPath.moveTo(-halfHandWidth, handLength - halfHandWidth * 4);
            clipPath.lineTo(halfHandWidth / 2, handLength - halfHandWidth * 4);
            clipPath.lineTo(halfHandWidth / 2, handLength - halfHandWidth * 4 + halfHandWidth / 4);
            clipPath.lineTo(-halfHandWidth, handLength - halfHandWidth * 4 + halfHandWidth / 4);
            clipPath.close();

            clipPath.moveTo(-halfHandWidth, handLength - halfHandWidth * 3);
            clipPath.lineTo(halfHandWidth / 2, handLength - halfHandWidth * 3);
            clipPath.lineTo(halfHandWidth / 2, handLength - halfHandWidth * 3 + halfHandWidth / 4);
            clipPath.lineTo(-halfHandWidth, handLength - halfHandWidth * 3 + halfHandWidth / 4);
            clipPath.close();
        } else {
            clipPath = null;
        }
    }

    void draw(Canvas canvas, float angleDegrees) {
        canvas.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(angleDegrees);
        if (highQuality) {
            canvas.drawPath(path, shadowPaint);
        }
        canvas.drawPath(path, fillPaint);
        if (clipPath != null) {
            canvas.drawPath(clipPath, clipPaint);
        }
        canvas.restore();
    }

    void updateHighQuality(boolean highQuality) {
        this.highQuality = highQuality;
        fillPaint.setAntiAlias(highQuality);
        clipPaint.setAntiAlias(highQuality);
    }
}
