package org.trypticon.dozenalwatchface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

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

    private final Paint fillPaint;
    private final Paint clipPaint;

    private final Matrix matrix = new Matrix();

    Hand(Context context,
         int widthId, int fillColorId, int strokeWidthId,
         float centerX, float centerY, float handLength, boolean pointy, boolean clip) {

        this.centerX = centerX;
        this.centerY = centerY;

        float halfHandWidth = context.getResources().getDimension(widthId) / 2;

        int fillColor = Workarounds.getColor(context, fillColorId);

        fillPaint = new Paint();
        fillPaint.setColor(fillColor);
        fillPaint.setStrokeWidth(context.getResources().getDimension(strokeWidthId));
        fillPaint.setStrokeCap(Paint.Cap.BUTT);
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        fillPaint.setAntiAlias(true);

        clipPaint = new Paint();
        clipPaint.setColor(Color.BLACK);
        clipPaint.setStrokeWidth(2.0f);
        clipPaint.setStrokeCap(Paint.Cap.SQUARE);
        clipPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        clipPaint.setAntiAlias(true);

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
        canvas.drawPath(path, fillPaint);

        if (clipPath != null) {
            canvas.drawPath(clipPath, clipPaint);
        }
    }

    void updateHighQuality(boolean highQuality) {
        fillPaint.setAntiAlias(highQuality);
        clipPaint.setAntiAlias(highQuality);
    }
}
