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

import org.trypticon.android.love39watchface.DateStyle;
import org.trypticon.android.love39watchface.R;
import org.trypticon.android.love39watchface.TimeStyle;
import org.trypticon.android.love39watchface.framework.WatchShape;
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

    @Override
    public int getIntrinsicWidth() {
        return 400;
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (400 * (1 - shape.getChinRatio()));
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

        System.out.println("Our bounds: " + bounds);

        // Parent sets our size to match the ratio declared in getIntrinsic*,
        // but when drawing, we want to know the size of the full circle and the children
        // will paint themselves as if these insets do not exist, just like what happens
        // on the actual watch face.
        Rect fullBounds = new Rect(bounds);
        fullBounds.bottom = (int) (fullBounds.top + (fullBounds.height() / (1 - shape.getChinRatio())));

        System.out.println("Painting border using bounds: " + fullBounds);

        float borderWidth = borderPaint.getStrokeWidth();
        borderPath.reset();
        if (shape.isRound()) {
            float chinRatio = shape.getChinRatio();
            if (chinRatio > 0.0f) {
                // Angle from the horizontal to the line from centre to the right of the chin.
                // Android angles are backwards so this all reads like it draws the mirror image,
                // but it doesn't...
                float startAngle = (float) Math.toDegrees(Math.asin(
                        (fullBounds.centerY() - (fullBounds.height() * chinRatio)) / fullBounds.centerX()));
                borderPath.addArc(
                        fullBounds.left + borderWidth / 2.0f, fullBounds.top + borderWidth / 2.0f,
                        fullBounds.right - borderWidth / 2.0f, fullBounds.bottom - borderWidth / 2.0f,
                        startAngle, -startAngle * 2 - 180);
                borderPath.close();
            } else {
                borderPath.addCircle(fullBounds.centerX(), fullBounds.centerY(),
                        fullBounds.centerX() - borderWidth / 2.0f,
                        Path.Direction.CCW);
            }
        } else {
            borderPath.addRect(borderWidth / 2.0f, borderWidth / 2.0f,
                    fullBounds.width() - borderWidth / 2.0f, fullBounds.height() - borderWidth / 2.0f,
                    Path.Direction.CCW);
        }

        layer.setBounds((int) (fullBounds.left + borderWidth), (int) (fullBounds.top + borderWidth),
                (int) (fullBounds.right - borderWidth), (int) (fullBounds.bottom - borderWidth));

        System.out.println("Painting layers at: " + layer.getBounds());

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
