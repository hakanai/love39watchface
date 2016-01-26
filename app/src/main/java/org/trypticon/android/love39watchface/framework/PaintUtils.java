package org.trypticon.android.love39watchface.framework;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Paint;
import android.support.annotation.ColorRes;

import org.trypticon.android.love39watchface.R;

/**
 * Utilities for working with paints.
 */
public class PaintUtils {
    private PaintUtils() {
    }

    public static Paint createGlowPaint(Context context, @ColorRes int baseColorKey) {
        float glowWidth = context.getResources().getDimension(R.dimen.analog_blur_width);
        Paint paint = new Paint();
        paint.setColor((Workarounds.getColor(context, baseColorKey) & 0xFFFFFF) | 0x40000000);
        paint.setMaskFilter(new BlurMaskFilter(glowWidth, BlurMaskFilter.Blur.NORMAL));
        paint.setStrokeWidth(glowWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        return paint;
    }

}
