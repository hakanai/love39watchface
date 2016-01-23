package org.trypticon.dozenalwatchface;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Ticks for a dozenal clock.
 */
class DozenalTicks extends Ticks {
    private static final int SUBDIVISIONS = 6;

    DozenalTicks(Context context) {
        super(context, SUBDIVISIONS);
    }

    @Override
    boolean isZeroAtTop() {
        return false;
    }

    @Override
    Drawable[] getDigits(Context context) {
        return new Drawable[] {
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
    }
}
