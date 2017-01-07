/*
 * Copyright © 2016-2017 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 */

package org.trypticon.android.love39watchface.framework;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Paint;
import android.support.annotation.ColorRes;

/**
 * Utilities for working with paints.
 */
public class PaintUtils {
    private PaintUtils() {
    }

    public static Paint createGlowPaint(Context context, @ColorRes int baseColorKey, float glowWidth) {
        Paint paint = new Paint();
        paint.setColor((Workarounds.getColor(context, baseColorKey) & 0xFFFFFF) | 0x40000000);
        paint.setMaskFilter(new BlurMaskFilter(glowWidth, BlurMaskFilter.Blur.NORMAL));
        paint.setStrokeWidth(glowWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        return paint;
    }

}
