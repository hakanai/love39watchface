package org.trypticon.dozenalwatchface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;

/**
 * Draws the ticks on the watch face.
 */
public class Ticks extends Drawable {
    private static final int SUBDIVISIONS = 6;

    private boolean round;
    private final Drawable[] digits;
    private final Drawable[] ticks;
    private final Paint primaryTickStroke;
    private final Paint secondaryTickStroke;

    public Ticks(Context context) {
        digits = new Drawable[] {
                context.getDrawable(R.drawable.digit0),
                context.getDrawable(R.drawable.digit1),
                context.getDrawable(R.drawable.digit2),
                context.getDrawable(R.drawable.digit3),
                context.getDrawable(R.drawable.digit4),
                context.getDrawable(R.drawable.digit5),
                context.getDrawable(R.drawable.digit6),
                context.getDrawable(R.drawable.digit7),
                context.getDrawable(R.drawable.digit8),
                context.getDrawable(R.drawable.digit9),
                context.getDrawable(R.drawable.digitdec),
                context.getDrawable(R.drawable.digitel),
        };
        ticks = new Drawable[12 * SUBDIVISIONS];

        primaryTickStroke = new Paint();
        primaryTickStroke.setColor(Workarounds.getColor(context, R.color.analog_primary_tick_stroke));
        primaryTickStroke.setStrokeWidth(context.getResources().getDimension(R.dimen.analog_primary_tick_stroke));
        primaryTickStroke.setAntiAlias(true);
        primaryTickStroke.setStrokeCap(Paint.Cap.SQUARE);
        primaryTickStroke.setStyle(Paint.Style.FILL_AND_STROKE);

        secondaryTickStroke = new Paint();
        secondaryTickStroke.setColor(Workarounds.getColor(context, R.color.analog_secondary_tick_stroke));
        secondaryTickStroke.setStrokeWidth(context.getResources().getDimension(R.dimen.analog_secondary_tick_stroke));
        secondaryTickStroke.setAntiAlias(true);
        secondaryTickStroke.setStrokeCap(Paint.Cap.SQUARE);
        secondaryTickStroke.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updateGeometry();
    }

    void updateShape(boolean round) {
        this.round = round;
        updateGeometry();
    }

    void updateHighQuality(boolean highQuality) {
        primaryTickStroke.setAntiAlias(highQuality);
        secondaryTickStroke.setAntiAlias(highQuality);
    }

    float computeTickDistance(float baseTickDistance, int degrees) {
        if (round) {
            return baseTickDistance;
        } else {
            double radians = Math.toRadians(degrees % 90);
            double factor = 2.5;
            return (float) (baseTickDistance / (
                    Math.pow((Math.pow(Math.cos(radians), factor) +
                                    Math.pow(Math.sin(radians), factor)),
                            1.0f/factor)));
        }
    }

    /**
     * Updates all the geometry so that we don't have to do all the maths at drawing-time.
     */
    private void updateGeometry() {
        float width = getBounds().width();
        float height = getBounds().height();
        float centerX = getBounds().centerX();
        float centerY = getBounds().centerY();
        float baseTickDistance = getBounds().centerX();

        for (int i = 0, degrees = 0; i < 12 * SUBDIVISIONS; i++, degrees += 360 / (12 * SUBDIVISIONS)) {
            float tickDistance = computeTickDistance(baseTickDistance, degrees);
            double radians = Math.toRadians(degrees);
            float directionX = - (float) Math.sin(radians);
            float directionY = + (float) Math.cos(radians);

            if (i % SUBDIVISIONS == 0) {
                // primary tick
                Path path = new Path();
                path.moveTo(
                        centerX + directionX * (tickDistance + 2),
                        centerY + directionY * (tickDistance + 2));
                path.lineTo(
                        centerX + directionX * (tickDistance * 2),
                        centerY + directionY * (tickDistance * 2));
                ShapeDrawable tick = new ShapeDrawable(new PathShape(path, width, height));
                tick.getPaint().set(primaryTickStroke);
                tick.setBounds(getBounds());
                ticks[i] = tick;

                float digitDistance = tickDistance - digits[0].getIntrinsicHeight();
                float digitX = centerX + digitDistance * directionX;
                float digitY = centerY + digitDistance * directionY;

                Drawable digitDrawable = digits[i / SUBDIVISIONS];
                digitDrawable.setBounds(
                        (int) (digitX - digitDrawable.getIntrinsicWidth() / 2.0f),
                        (int) (digitY - digitDrawable.getIntrinsicHeight() / 2.0f),
                        (int) (digitX + digitDrawable.getIntrinsicWidth() / 2.0f),
                        (int) (digitY + digitDrawable.getIntrinsicHeight() / 2.0f));

            } else {
                // secondary tick
                Path path = new Path();
                path.moveTo(
                        centerX + directionX * (tickDistance - 8),
                        centerY + directionY * (tickDistance - 8));
                path.lineTo(
                        centerX + directionX * (tickDistance * 2),
                        centerY + directionY * (tickDistance * 2));
                ShapeDrawable tick = new ShapeDrawable(new PathShape(path, width, height));
                tick.getPaint().set(secondaryTickStroke);
                tick.setBounds(getBounds());
                ticks[i] = tick;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for (Drawable digit : digits) {
            digit.draw(canvas);
        }
        for (Drawable tick : ticks) {
            tick.draw(canvas);
        }
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
        return PixelFormat.TRANSLUCENT;
    }
}
