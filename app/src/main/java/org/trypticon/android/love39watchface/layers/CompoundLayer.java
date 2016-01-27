package org.trypticon.android.love39watchface.layers;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.ustwo.clockwise.WatchShape;

import org.trypticon.android.love39watchface.framework.WatchModeHelper;
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
    public void draw(Canvas canvas) {
        for (Layer layer : layers) {
            layer.draw(canvas);
        }
    }
}
