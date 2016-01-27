package org.trypticon.android.love39watchface.config;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.ustwo.clockwise.WatchFaceTime;
import com.ustwo.clockwise.WatchShape;

import org.trypticon.android.love39watchface.DateStyle;
import org.trypticon.android.love39watchface.R;
import org.trypticon.android.love39watchface.TimeStyle;
import org.trypticon.android.love39watchface.framework.Workarounds;
import org.trypticon.android.love39watchface.layers.Layer;
import org.trypticon.android.love39watchface.layers.LayerFactory;
import org.trypticon.android.love39watchface.time.MultiTime;

/**
 * A drawable that renders a sample of what the watch face would show for a single setting.
 */
class SampleDrawable extends Drawable {
    private final WatchShape shape;
    private final Path borderPath;
    private final Paint borderPaint;
    private final Paint borderPaintChecked;

    private final Layer layer;

    private boolean checked;

    SampleDrawable(Context context, WatchShape shape, Layer layer) {
        this.shape = shape;
        this.layer = layer;
        layer.updateShape(shape);

        borderPath = new Path();
        borderPaint = new Paint();
        borderPaint.setColor(Workarounds.getColor(context, R.color.config_list_item_border));
        borderPaint.setStrokeWidth(context.getResources().getDimension(R.dimen.config_list_item_border));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);

        borderPaintChecked = new Paint(borderPaint);
        borderPaintChecked.setColor(Workarounds.getColor(context, R.color.config_list_item_border_checked));

    }

    static SampleDrawable createForTime(Context context, WatchShape shape, TimeStyle timeStyle) {
        return new SampleDrawable(context, shape, LayerFactory.createTimeStyleConfigLayers(context, timeStyle));
    }

    static SampleDrawable createForDate(Context context, WatchShape shape, DateStyle dateStyle) {
        return new SampleDrawable(context, shape, LayerFactory.createDateStyleConfigLayers(context, dateStyle));
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    protected boolean onStateChange(int[] state) {
        boolean checked = false;
        for (int value : state) {
            if (value == android.R.attr.state_checked || value == android.R.attr.state_activated) {
                checked = true;
                break;
            }
        }

        if (this.checked != checked) {
            this.checked = checked;
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        float borderWidth = borderPaint.getStrokeWidth();
        borderPath.reset();
        if (shape == WatchShape.CIRCLE) {
            borderPath.addCircle(bounds.centerX(), bounds.centerY(),
                    bounds.centerX() - borderWidth / 2.0f,
                    Path.Direction.CCW);
        } else {
            borderPath.addRect(borderWidth / 2.0f, borderWidth / 2.0f,
                    bounds.width() - borderWidth / 2.0f, bounds.height() - borderWidth / 2.0f,
                    Path.Direction.CCW);
        }

        layer.setBounds((int) (bounds.left + borderWidth), (int) (bounds.top + borderWidth),
                (int) (bounds.right - borderWidth), (int) (bounds.bottom - borderWidth));

        MultiTime time = new MultiTime();
        WatchFaceTime watchFaceTime = new WatchFaceTime();
        watchFaceTime.setToNow();
        time.setTo(watchFaceTime);
        layer.updateTime(time);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        try {
            canvas.clipPath(borderPath);
            layer.draw(canvas);
        } finally {
            canvas.restore();
        }

        canvas.drawPath(borderPath, checked ? borderPaintChecked : borderPaint);
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
