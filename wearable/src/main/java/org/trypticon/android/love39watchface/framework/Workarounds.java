/*
 * Copyright © 2016-2017 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 */

package org.trypticon.android.love39watchface.framework;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

/**
 * Collection of Android SDK workarounds.
 */
public class Workarounds {

    /**
     * Prevents instantiation.
     */
    private Workarounds() {
    }

    /**
     * Looks up a colour in the context.
     * Method exists solely as an Android SDK workaround for deprecation warnings.
     *
     * @param context the context.
     * @param id the ID of the resource.
     * @return the colour.
     */
    @SuppressWarnings("deprecation")
    public static int getColor(Context context, @ColorRes int id) {
        return context.getResources().getColor(id);
    }

    /**
     * Looks up a drawable in the context.
     * Method exists solely as an Android SDK workaround for deprecation warnings.
     *
     * @param context the context.
     * @param id the ID of the resource.
     * @return the colour.
     */
    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(Context context, @DrawableRes int id) {
        return context.getResources().getDrawable(id);
    }
}
