package org.trypticon.dozenalwatchface;

import android.content.res.Resources;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Encapsulation of information about a single hand.
 */
class Hand {
    private static final float startOffset = 12;

    private final float centerX;
    private final float centerY;
    private final float halfHandWidth;
    private final float handLength;

    private final Paint fillPaint;
    private final Paint shadowPaint;

    private boolean highQuality = true;

    Hand(Resources resources,
         int widthId, int fillColorId, int strokeWidthId, int shadowWidthId,
         float centerX, float centerY, float handLength) {

        this.centerX = centerX;
        this.centerY = centerY;
        this.handLength = handLength;

        halfHandWidth = resources.getDimension(widthId) / 2;

        int fillColor = Workarounds.getColor(resources, fillColorId);

        fillPaint = new Paint();
        fillPaint.setColor(fillColor);
        fillPaint.setStrokeWidth(resources.getDimension(strokeWidthId));
        fillPaint.setStrokeCap(Paint.Cap.ROUND);
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        fillPaint.setAntiAlias(true);

        // I found Paint#setShadowLayer to be a bit crap. :(
        float shadowWidth = resources.getDimension(shadowWidthId);
        shadowPaint = new Paint(fillPaint);
        shadowPaint.setColor((fillColor & 0xFFFFFF) | 0x40000000);
        shadowPaint.setStrokeWidth(shadowWidth);
        shadowPaint.setMaskFilter(new BlurMaskFilter(shadowWidth / 2.0f, BlurMaskFilter.Blur.NORMAL));

    }

    void draw(Canvas canvas, float angleDegrees) {
        canvas.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(angleDegrees);
        if (highQuality) {
            canvas.drawRect(-halfHandWidth, startOffset, halfHandWidth, handLength, shadowPaint);
        }
        canvas.drawRect(-halfHandWidth, startOffset, halfHandWidth, handLength, fillPaint);
        canvas.restore();
    }

    void updateHighQuality(boolean highQuality) {
        this.highQuality = highQuality;
        fillPaint.setAntiAlias(highQuality);
    }
}
