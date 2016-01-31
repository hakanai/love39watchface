package org.trypticon.android.love39watchface.layers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import org.trypticon.android.love39watchface.R;
import org.trypticon.android.love39watchface.framework.PaintHolder;
import org.trypticon.android.love39watchface.framework.WatchModeHelper;
import org.trypticon.android.love39watchface.framework.Workarounds;
import org.trypticon.android.love39watchface.time.DateFormat;
import org.trypticon.android.love39watchface.time.MultiTime;
import org.trypticon.android.love39watchface.time.Time;
import org.trypticon.android.love39watchface.time.TimeSystem;

/**
 * Layer which draws the date.
 */
class DateLayer extends Layer {
    private final Context context;
    private final TimeSystem timeSystem;
    private final DateFormat dateFormat;
    private PaintHolder datePaint;

    private Time time;

    DateLayer(final Context context, TimeSystem timeSystem, DateFormat dateFormat) {
        this.context = context;
        this.timeSystem = timeSystem;
        this.dateFormat = dateFormat;

        updateDatePaint(400);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        updateDatePaint(bounds.width());
    }

    private void updateDatePaint(final float width) {
        datePaint = new PaintHolder(false) {
            @Override
            protected void configure(Paint paint) {
                Typeface roboto = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
                paint.setTypeface(roboto);
                paint.setColor(Workarounds.getColor(context, R.color.date_fill));
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(width * Proportions.DATE_SIZE);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
            }
        };
    }

    @Override
    public void updateWatchMode(WatchModeHelper mode) {
        datePaint.updateWatchMode(mode);
    }

    @Override
    public void updateTime(MultiTime time) {
        this.time = time.getTime(timeSystem);
    }

    @Override
    public void draw(Canvas canvas) {
        float width = getBounds().width();

        canvas.drawText(
                dateFormat.formatDate(time),
                width * Proportions.DATE_X,
                width * Proportions.DATE_Y, datePaint.getPaint());
    }
}
