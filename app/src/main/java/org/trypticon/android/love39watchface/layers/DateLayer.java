package org.trypticon.android.love39watchface.layers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
public class DateLayer extends Layer {
    private final TimeSystem timeSystem;
    private final DateFormat dateFormat;
    private final PaintHolder datePaint;

    private Time time;

    public DateLayer(final Context context, TimeSystem timeSystem, DateFormat dateFormat) {
        this.timeSystem = timeSystem;
        this.dateFormat = dateFormat;

        datePaint = new PaintHolder(false) {
            @Override
            protected void configure(Paint paint) {
                Typeface roboto = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
                paint.setTypeface(roboto);
                paint.setColor(Workarounds.getColor(context, R.color.date_fill));
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(context.getResources().getDimension(R.dimen.date_size));
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
        float centerX = canvas.getWidth() / 2;
        float centerY = canvas.getHeight() / 2;

        canvas.drawText(
                dateFormat.formatDate(time),
                centerX, centerY / 2, datePaint.getPaint());
    }
}
