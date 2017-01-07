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
import android.graphics.Rect;
import android.support.annotation.NonNull;

import org.trypticon.android.love39watchface.R;
import org.trypticon.android.love39watchface.framework.PaintHolder;
import org.trypticon.android.love39watchface.framework.WatchModeHelper;
import org.trypticon.android.love39watchface.framework.Workarounds;

/**
 * Layer which draws the background.
 */
class BackgroundLayer extends Layer {
    private final PaintHolder backgroundPaint;

    private final Rect tempRect = new Rect();

    BackgroundLayer(final Context context) {
        backgroundPaint = new PaintHolder(true) {
            @Override
            protected void configure(Paint paint) {
                paint.setColor(Workarounds.getColor(context, R.color.analog_background));
            }
        };
    }

    @Override
    public void updateWatchMode(WatchModeHelper mode) {
        backgroundPaint.updateWatchMode(mode);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (canvas.getClipBounds(tempRect)) {
            canvas.drawRect(tempRect, backgroundPaint.getPaint());
        }
    }

}
