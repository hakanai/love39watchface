package org.trypticon.android.love39watchface.config;

import android.content.Context;
import android.support.annotation.StringRes;

import org.trypticon.android.love39watchface.R;
import org.trypticon.android.love39watchface.framework.WatchShape;

/**
 * Enumeration of ways to display the time.
 */
public enum TimeStyle {
    CLASSIC(R.string.config_time_classic),
    
    DOZENAL(R.string.config_time_dozenal);

    @StringRes
    private final int stringKey;

    TimeStyle(@StringRes int stringKey) {
        this.stringKey = stringKey;
    }

    PreviewListView.ListItem<TimeStyle> createListItem(Context context, WatchShape shape) {
        return new PreviewListView.ListItem<>(
                this,
                SampleDrawable.createForTime(context, shape, this),
                context.getString(stringKey));
    }

}
