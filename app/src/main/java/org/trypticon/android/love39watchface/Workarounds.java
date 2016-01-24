package org.trypticon.android.love39watchface;

import android.content.Context;
import android.support.annotation.ColorRes;

/**
 * Collection of Android SDK workarounds.
 */
class Workarounds {

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
    static int getColor(Context context, @ColorRes int id) {
        return context.getResources().getColor(id);
    }

}
