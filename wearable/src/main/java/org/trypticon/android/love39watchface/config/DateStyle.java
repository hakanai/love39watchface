/*
 * Copyright © 2016-2017 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 */

package org.trypticon.android.love39watchface.config;

import android.content.Context;
import android.support.annotation.StringRes;

import org.trypticon.android.love39watchface.R;
import org.trypticon.android.love39watchface.framework.WatchShape;

/**
 * Enumeration of ways to display the date.
 */
public enum DateStyle {
    CLASSIC(R.string.config_calendar_classic),

    DOZENAL(R.string.config_calendar_dozenal);

    private final int stringKey;

    DateStyle(@StringRes int stringKey) {
        this.stringKey = stringKey;
    }

    PreviewListView.ListItem<DateStyle> createListItem(Context context, WatchShape shape) {
        return new PreviewListView.ListItem<>(
                this,
                SampleDrawable.createForDate(context, shape, this),
                context.getString(stringKey));
    }
}
