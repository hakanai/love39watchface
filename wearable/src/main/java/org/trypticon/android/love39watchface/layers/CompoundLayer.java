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

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import org.trypticon.android.love39watchface.framework.WatchModeHelper;
import org.trypticon.android.love39watchface.framework.WatchShape;
import org.trypticon.android.love39watchface.time.MultiTime;

/**
 * Multiple layers stacked on top of one another.
 */
class CompoundLayer extends Layer {
    private final Layer[] layers;

    CompoundLayer(Layer... layers) {
        this.layers = layers;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        for (Layer layer : layers) {
            layer.setBounds(bounds);
        }
    }

    @Override
    public void updateWatchMode(WatchModeHelper mode) {
        for (Layer layer : layers) {
            layer.updateWatchMode(mode);
        }
    }

    @Override
    public void updateShape(WatchShape shape) {
        for (Layer layer : layers) {
            layer.updateShape(shape);
        }
    }

    @Override
    public void updateTime(MultiTime time) {
        for (Layer layer : layers) {
            layer.updateTime(time);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        for (Layer layer : layers) {
            layer.draw(canvas);
        }
    }
}
