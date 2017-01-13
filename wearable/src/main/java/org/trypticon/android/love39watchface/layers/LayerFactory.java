/*
 * Copyright © 2016-2017 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 *
 * This file additionally may contain code which programmatically renders
 * representations of elements of Hatsune Miku's design. Such art-as-code is
 * subject to the terms and conditions of the Creative Commons -
 * Attribution-NonCommercial, 3.0 Unported (CC BY-NC) licence.
 * See the Copying.CC_BY_NC file for more details.
 */

package org.trypticon.android.love39watchface.layers;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.trypticon.android.love39watchface.R;
import org.trypticon.android.love39watchface.config.DateStyle;
import org.trypticon.android.love39watchface.config.TimeStyle;
import org.trypticon.android.love39watchface.time.TimeSystem;

/**
 * Factory for creating layers.
 */
public class LayerFactory {

    public static Layer createWatchFaceLayers(Context context, TimeStyle timeStyle, DateStyle dateStyle) {
        return new CompoundLayer(
                createBackground(context),
                createHeart(context),
                createDate(context, dateStyle),
                createTicks(context, timeStyle),
                createHands(context, timeStyle));
    }

    public static Layer createTimeStyleConfigLayers(Context context, TimeStyle timeStyle) {
        return new CompoundLayer(
                createBackground(context),
                createTicks(context, timeStyle),
                createHands(context, timeStyle));
    }

    public static Layer createDateStyleConfigLayers(Context context, DateStyle dateStyle) {
        return new CompoundLayer(
                createBackground(context),
                createDate(context, dateStyle));
    }

    private static Layer createBackground(Context context) {
        return new BackgroundLayer(context);
    }

    private static Layer createHeart(Context context) {
        return new HeartLayer(context);
    }

    private static Layer createDate(Context context, DateStyle dateStyle) {
        switch (dateStyle) {
            case CLASSIC:
                return new DateLayer(context, TimeSystem.CLASSIC);
            case DOZENAL:
                return new DateLayer(context, TimeSystem.DOZENAL);
            default:
                throw new IllegalStateException("Unimplemented date style: " + dateStyle);
        }
    }

    private static Layer createTicks(Context context, TimeStyle timeStyle) {
        switch (timeStyle) {
            case CLASSIC: {
                Drawable[] digits = {
                        context.getDrawable(R.drawable.digit12),
                        context.getDrawable(R.drawable.digit1),
                        context.getDrawable(R.drawable.digit2),
                        context.getDrawable(R.drawable.digit3),
                        context.getDrawable(R.drawable.digit4),
                        context.getDrawable(R.drawable.digit5),
                        context.getDrawable(R.drawable.digit6),
                        context.getDrawable(R.drawable.digit7),
                        context.getDrawable(R.drawable.digit8),
                        context.getDrawable(R.drawable.digit9),
                        context.getDrawable(R.drawable.digit10),
                        context.getDrawable(R.drawable.digit11),
                };
                return new TicksLayer(context, 5, true, digits);
            }

            case DOZENAL: {
                Drawable[] digits = {
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

                return new TicksLayer(context, 6, false, digits);
            }

            default:
                throw new IllegalStateException("Unimplemented tick style: " + timeStyle);
        }
    }

    private static HandsLayer createHands(Context context, TimeStyle timeStyle) {
        switch (timeStyle) {
            case CLASSIC:
                return new HandsLayer(context, TimeSystem.CLASSIC, true, false);

            case DOZENAL:
                return new HandsLayer(context, TimeSystem.DOZENAL, false, true);

            default:
                throw new IllegalStateException("Unimplemented hand style: " + timeStyle);
        }
    }
}
