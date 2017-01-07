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

/**
 * Holder for dimensions which are done as a percentage of screen size.
 */
class Proportions {
    static final float DATE_X                = 200 / 400f;
    static final float DATE_Y                = 100 / 400f;
    static final float DATE_SIZE             =  40 / 400f;

    static final float LARGE_HAND_WIDTH      =  24 / 400f;
    static final float SMALL_HAND_WIDTH      =   4 / 400f;
    static final float CENTRE_RADIUS         = LARGE_HAND_WIDTH / 2;

    static final float HAND_START_RADIUS     =  20 / 400f;
    static final float HOUR_HAND_FROM_EDGE   =  60 / 400f;
    static final float MINUTE_HAND_FROM_EDGE =  40 / 400f;
    static final float SECOND_HAND_FROM_EDGE =  35 / 400f;
    static final float THIRD_HAND_FROM_EDGE  =  30 / 400f;
    static final float HAND_GLOW_WIDTH       =  22 / 400f;

    static final float HEART_X               = 200 / 400f;
    static final float HEART_Y               = 280 / 400f;
    static final float HEART_WIDTH           = 192 / 400f;
    static final float HEART_HEIGHT          = 144 / 400f;
    static final float HEART_GLOW_WIDTH      = HAND_GLOW_WIDTH;

    static final float MAJOR_TICK_FROM_EDGE  =   2 / 400f;
    static final float MINOR_TICK_FROM_EDGE  =  10 / 400f;

    // Even though this is the width, digits with a wider intrinsic width than the others will be wider.
    static final float DIGIT_WIDTH           =  24 / 400f;
    static final float DIGIT_HEIGHT          =  24 / 400f;

}
