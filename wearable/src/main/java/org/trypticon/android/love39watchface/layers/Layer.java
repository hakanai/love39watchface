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

import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import org.trypticon.android.love39watchface.framework.WatchModeAware;
import org.trypticon.android.love39watchface.framework.WatchModeHelper;
import org.trypticon.android.love39watchface.framework.WatchShape;
import org.trypticon.android.love39watchface.time.MultiTime;

/**
 * Base class for a drawable layer.
 */
public abstract class Layer extends Drawable implements WatchModeAware {
    @Override
    public void updateWatchMode(WatchModeHelper mode) {

    }

    public void updateShape(WatchShape shape) {

    }

    public void updateTime(MultiTime time) {

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
